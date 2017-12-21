
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class mainApp extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;                                                //the main frame containing everything
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem menuitem;
	private JFileChooser fc;
	private JLabel emptyLabel1, emptyLabelcon;                           //initial text labels present at app initialisation
	private JPanel jp1, jp2;                                             //the two tabs
	private JTabbedPane jtp;                                             //the tabbed pane
	private JScrollPane scroolAll;
	private JScrollPane jemptyLabel;
	private FileNameExtensionFilter filter;
	
	private final Object aboutMessage = "Program created by: \n\n Dimitris *********, 31****** \n Niki ********, 3*******";
	private final Object helpMessage = "How to use this application:\n\n1)Load services txt from menu,\n2)Load contracts txt,"
			+ "\n3)Perform any other operation made available,\n4)Save any changes made to contracts txt before exiting app.";
	private Object sloadMessage;
	private Object cloadMessage;
	
	private boolean thereAreServices = false;                                //for successful services load check
	private boolean thereAreContracts = false;								 //for successful contracts load check
	private ServiceRead readServices;
	private ContractReadWrite readContracts;
	private String filepathS;
	private String filepathC;
	
	private ArrayList<Contract> contracts = new ArrayList<Contract>();       //list where contract objects are stored
	private ArrayList<Service> services;                                     //list where service objects are stored
	private File contractsfile;                                              //the txt file for contracts
	private File servicesfile;                                               //the txt file for services
	
	private Font font = new Font("Serif", Font.BOLD, 19);                    
	private Font fontC = new Font("Serif", Font.BOLD, 19);
	
	//fields for contract creation
	private JTextField cname = new JTextField(5);                            
    private JTextField cnumber = new JTextField(5);
    private JTextField pmeth = new JTextField(5);
    private JTextField adddisc = new JTextField(5);
	
    
	public mainApp() {
		
		//create filechooser and set .txt filter
		fc = new JFileChooser();
		filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fc.setFileFilter(filter);
		
		//create the main frame and add a tabbed pane to it
		frame = new JFrame();
        jtp = new JTabbedPane(); 
        frame.add(jtp); 

        //create the two tabs, set a pre-file-loaded view for each of them
        JLabel emptyLabel0 = new JLabel(" ");
        emptyLabel1 = new JLabel("Please load some Services to enable this tab's functionality.");
        emptyLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyLabel1.setForeground(Color.RED);
        emptyLabel1.setFont(font);
        jp1 = new JPanel();
        jp1.setLayout(new BoxLayout(jp1, BoxLayout.Y_AXIS));
        jp1.add(emptyLabel0);
        jp1.add(emptyLabel1);
        
        JLabel emptyLabel9 = new JLabel(" ");
        emptyLabelcon = new JLabel("Please load some Contracts to enable this tab's functionality.");
        emptyLabelcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyLabelcon.setForeground(Color.BLUE);
        emptyLabelcon.setFont(fontC);
        jp2 = new JPanel();
        jp2.setLayout(new BoxLayout(jp2, BoxLayout.Y_AXIS));
        jp2.add(emptyLabel9);
        jp2.add(emptyLabelcon);
        
        //add the two tabs to the tabbed pane
        jtp.addTab("Available Services", null,  jp1, "Display Available Services");
        jtp.addTab("Active Contracts", null, jp2, "Display Active Contracts");

		//menu
		menubar = new JMenuBar();
		menu = new JMenu("File");                                   //load the services txt file
		menuitem = new JMenuItem("Load Services from file");
		menuitem.setMnemonic(KeyEvent.VK_S);
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if (!thereAreServices){
					
					int returnVal = fc.showOpenDialog(mainApp.this);
					if(returnVal == JFileChooser.APPROVE_OPTION){
						servicesfile = fc.getSelectedFile();
		                filepathS = servicesfile.getAbsolutePath();
		                System.out.println("File loaded: " + servicesfile.getName() + ".");
		                readServices = new ServiceRead();
		        		readServices.loadfile(filepathS);
		        		
		        		services = readServices.getList();
		        		
		        		if (!readServices.getList().isEmpty()){
		        			System.out.println(readServices.getList().size()+ " services loaded successfully from " + servicesfile.getName() + "\n");
		        			thereAreServices = true;
		        		}
		        		else{
		        			System.err.println("No services were found in " + servicesfile.getName());
		        		}
		        		
		        		if(!thereAreServices){
		        			sloadMessage = readServices.getList().size() + " services were loaded from " + servicesfile.getName() + "\n"+"Please Try again.";
		        			JOptionPane.showMessageDialog(null, sloadMessage, "Open Result", JOptionPane.INFORMATION_MESSAGE);
		        		}
		        		else{
			        		sloadMessage = readServices.getList().size() + " services were loaded from " + servicesfile.getName();
			        		JOptionPane.showMessageDialog(null, sloadMessage, "Open Result", JOptionPane.INFORMATION_MESSAGE);
			        		emptyLabel1.setVisible(false);
			        		servicesTabFunctionality();
		        		}
					}
					else{
						
						System.out.println("Open command cancelled by user.");
					}
	                
	        		
				} 
				else{
					JOptionPane.showMessageDialog(null, "Services have already been loaded.", "Alert", JOptionPane.INFORMATION_MESSAGE);
	            }
			}
		});
		menu.add(menuitem);
		menuitem = new JMenuItem("Load Contracts from file");       //load the contracts txt file
		menuitem.setMnemonic(KeyEvent.VK_C);
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				if(!thereAreServices){
					JOptionPane.showMessageDialog(null, "Please load services first", "Alert", JOptionPane.INFORMATION_MESSAGE);
				}
				else if(thereAreContracts){
					JOptionPane.showMessageDialog(null, "Contracts have already been loaded.", "Alert", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					int returnVal = fc.showOpenDialog(mainApp.this);
					if (returnVal == JFileChooser.APPROVE_OPTION){
						
		                contractsfile = fc.getSelectedFile();
		                filepathC = contractsfile.getAbsolutePath();
		                System.out.println("File loaded: " + contractsfile.getName() + ".");
		                readContracts = new ContractReadWrite();
		        		readContracts.loadfile(filepathC, filepathS);
		        		
		        		contracts = readContracts.getList();
		        		
		        		if (!readContracts.getList().isEmpty()){
		        			System.out.println(readContracts.getList().size()+ " contracts loaded successfully from " + contractsfile.getName() + "\n");
		        			thereAreContracts = true;
		        		}
		        		else{
		        			System.err.println("No contracts were found in " + contractsfile.getName());
		        		}
		        		
		        		if(!thereAreContracts){
		        			cloadMessage = readContracts.getList().size() + " contracts were loaded from " + contractsfile.getName() + "\n"+"Please Try again.";
		        		}
		        		else{
			        		cloadMessage = readContracts.getList().size() + " contracts were loaded from " + contractsfile.getName();
			        		
			        		JOptionPane.showMessageDialog(null, cloadMessage, "Open Result", JOptionPane.INFORMATION_MESSAGE);
			        		emptyLabelcon.setVisible(false);
			        		contractsTabFunctionality();
			        		
		        		}
		        		
					}
					else{
						System.out.println("Open command cancelled by user.");
					}
				}

			}
		});
		menu.add(menuitem);
		menu.addSeparator();
		menuitem = new JMenuItem("Save");
		menuitem.setMnemonic(KeyEvent.VK_S);                        //saves any changes made to contracts file
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(thereAreContracts){
					int option = JOptionPane.showConfirmDialog(null, "Do you wish to save any changes made to contracts.txt?", "Save", JOptionPane.YES_NO_OPTION);
					if (option==0){
						System.out.println("yes");
						boolean saved = readContracts.writefile(filepathC, contracts);
						if(saved){
							JOptionPane.showMessageDialog(null, "Changes saved successfully to " + contractsfile.getName(), "Alert", JOptionPane.INFORMATION_MESSAGE);
						}
					}
					else{
						System.out.println("no");
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "You need to load some contracts first!", "Alert", JOptionPane.INFORMATION_MESSAGE);
				}

				System.out.println("Pressed 'Save' from menu.");
			}
		});
		menu.add(menuitem);
		menu.addSeparator();
		menuitem = new JMenuItem("Exit");                            //exit button using a confirmation dialog
		menuitem.setMnemonic(KeyEvent.VK_X);
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				int result = JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost.\nContinue?");
            	if (result==JOptionPane.OK_OPTION) {
            	    System.exit(0);     
            	}
				System.out.println("Pressed 'Exit' from menu.");
			}
		});
		menu.add(menuitem);
		menubar.add(menu);
		menu = new JMenu("About");
		menuitem = new JMenuItem("Credits");                         //show credits for the app
		menuitem.setMnemonic(KeyEvent.VK_C);
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Pressed 'Credits' from menu.");
				JOptionPane.showMessageDialog(null, aboutMessage, "Credits", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(menuitem);
		menu.addSeparator();
		menuitem = new JMenuItem("Help");                            //shows how to use program
		menuitem.setMnemonic(KeyEvent.VK_H);
		menuitem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.out.println("Pressed 'Help' from menu.");
				JOptionPane.showMessageDialog(null, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
			}
		});	
		menu.add(menuitem);
		menubar.add(menu);
		
		frame.setJMenuBar(menubar);                                   //sets frame's menubar created above
		
		Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension ( 1000, 400 );                        //sets the size of the main frame
		frame.setBounds ( ss.width / 2 - frameSize.width / 2, ss.height / 
				2 - frameSize.height / 2, frameSize.width, frameSize.height );    //center the frame on user's screen
		frame.setTitle("Telecommunications Manager");  
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   //tells frame not to close after user input
        frame.addWindowListener(new WindowAdapter() {                 //prompts a confirmation dialog for exiting the app
            public void windowClosing(WindowEvent e) {
            	
            	int result = JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost.\nContinue?");
            	if (result==JOptionPane.OK_OPTION) {
            	    System.exit(0);     
            	}
            }
        });
        
    }

    //creates table containing services of a specific type and adds it to 'available services' tab
    public JScrollPane GUITableServices(String service, boolean hide, JScrollPane scrollPaneR){
    	
    	JScrollPane scrollPane = null;
    	if(hide){
    		scrollPaneR.setVisible(false);
    	}
    	else{
        	int counter = 0;
        	for (int i=0; i<services.size(); i++){
        		if(String.valueOf(services.get(i).getClass()).substring(6).equals(service)){
        			counter++;
        		}
        	}
        	
        	String[] columnNames = {"Type", "Name", "Monthly Charge", " "};
        	Object[][] data = new Object[counter][4];
        	int k=0;
        	for (int i=0; i<services.size(); i++){
        		
        		if(String.valueOf(services.get(i).getClass()).substring(6).equals(service)){
        			
        			data[k][0] = String.valueOf(services.get(i).getClass()).substring(6);
            		data[k][1] = services.get(i).getName();
            		data[k][2] = services.get(i).getMonthlyCharge() + "";
            		data[k][3] = "Create Contract";
            		k++;
        		}
        		
        	}
        	JTable table = new JTable(data, columnNames);
        	
        	DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
        		private static final long serialVersionUID = 1L;

        		@Override
        	    public boolean isCellEditable(int row, int column) {
        			if (column==3){
        				return true;
        			}
        			else{
        				return false;
        			}
        	        
        	    }
        	};
        	table.setModel(tableModel);
        	table.getColumn(" ").setCellRenderer(new ButtonRenderer());
            table.getColumn(" ").setCellEditor(new ButtonEditor(new JCheckBox()));
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        	scrollPane = new JScrollPane(table);
        	table.setAlignmentX(Component.CENTER_ALIGNMENT);
        	
        	
        	table.addMouseListener(new MouseAdapter() {
        		  public void mouseClicked(MouseEvent e) {
        		      if (e.getClickCount() == 2) {
        		    	  
    	    		      JTable target = (JTable)e.getSource();
    	    		      int row = target.getSelectedRow();  
        		    	  try {
    						  JOptionPane.showMessageDialog(null, readServices.getService(service, String.valueOf(row+1)), "Service Details", JOptionPane.INFORMATION_MESSAGE);
    					  } 
        		    	  catch (HeadlessException e1) {
    						  e1.printStackTrace();
    					  } 
        		    	  catch (Exception e1) {
    						  e1.printStackTrace();
    					  }
    	    		      
        		      }
        		  }
        	});
        	
        	jp1.add(scrollPane, BorderLayout.CENTER);
    	}
    	return scrollPane;
    }
    
    //creates table containing contracts of a specific type of service and adds it to 'active contracts' tab
    public JScrollPane GUITableContracts(String service, boolean hide, JScrollPane scrollPaneR){
    	
    	JScrollPane scrollPane = null;
    	if(hide){
    		scrollPaneR.setVisible(false);
    	}
    	else{
        	int counter = 0;
        	for (int i=0; i<contracts.size(); i++){
        		
        		if(String.valueOf(contracts.get(i).getTypeOfService().getClass()).substring(6).equals(service)){
        			counter++;
        		}
        	}
        	
        	String[] columnNames = {"Code", "Customer Name", "Phone", "Activation Date", "Cost/Balance", "Free Data/Calls/Sms", "Statistics"};
        	Object[][] data = new Object[counter][7];
        	int k=0;
        	for (int i=0; i<contracts.size(); i++){
        		
        		if(String.valueOf(contracts.get(i).getTypeOfService().getClass()).substring(6).equals(service)){
        			
        			data[k][0] = "# " + contracts.get(i).getCode();
            		data[k][1] = contracts.get(i).getCustomerName();
            		data[k][2] = contracts.get(i).getCustomerTelNum();
            		data[k][3] = contracts.get(i).getActivationDate();
            		data[k][4] = "Calculate";
            		data[k][5] = "Calculate";
            		data[k][6] = "Update";
            		k++;
        		}
        		
        	}
        	JTable table = new JTable(data, columnNames);
        	
        	DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
        		private static final long serialVersionUID = 1L;

        		@Override
        	    public boolean isCellEditable(int row, int column) {
        			if (column==4||column==5||column==6){
        				return true;
        			}
        			else{
        				return false;
        			}
        	        
        	    }
        	};
        	table.setModel(tableModel);
        	table.getColumn("Statistics").setCellRenderer(new ButtonRenderer());
            table.getColumn("Statistics").setCellEditor(new ButtonEditorCU(new JCheckBox()));
            table.getColumn("Cost/Balance").setCellRenderer(new ButtonRenderer());
            table.getColumn("Cost/Balance").setCellEditor(new ButtonEditorCC(new JCheckBox()));
            table.getColumn("Free Data/Calls/Sms").setCellRenderer(new ButtonRenderer());
            table.getColumn("Free Data/Calls/Sms").setCellEditor(new ButtonEditorCR(new JCheckBox()));
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        	scrollPane = new JScrollPane(table);
        	table.setAlignmentX(Component.CENTER_ALIGNMENT);
        	
        	
        	table.addMouseListener(new MouseAdapter() {
        		  public void mouseClicked(MouseEvent e) {
        		      if (e.getClickCount() == 2) {
        		    	  
    	    		      JTable target = (JTable)e.getSource();
    	    		      int row = target.getSelectedRow();
    	    		      System.out.println(row);	  
        		    	  try {
        		    		  int z=0;
        		    		  for (int i=0; i<contracts.size(); i++){
        		    			  if(String.valueOf(contracts.get(i).getCode()).equals(String.valueOf(target.getModel().getValueAt(row, 0)).substring(2))){
        		    				  z = i;
        		    			  }
        		    		  }
    						  JOptionPane.showMessageDialog(null, contracts.get(z).toString(), "Contract Details", JOptionPane.INFORMATION_MESSAGE);
    					  } 
        		    	  catch (HeadlessException e1) {
    						  e1.printStackTrace();
    					  } 
        		    	  catch (Exception e1) {
    						  e1.printStackTrace();
    					  }
    	    		      
        		      }
        		  }
        	});
        	
        	jp2.add(scrollPane, BorderLayout.CENTER);
    	}
    	return scrollPane;
    }
    
    //creates table containing all of the current active contracts and adds it to 'active contracts' tab
    public JScrollPane GUITableContractsAll(boolean hide, JScrollPane scrollPaneR){
    	
    	JScrollPane scrollPane = null;
    	if(hide){
    		scrollPaneR.setVisible(false);
    	}
    	else{
        	
        	String[] columnNames = {"Code", "Customer Name", "Phone", "Activation Date", "Cost/Balance", "Free Data/Calls/Sms", "Statistics"};
        	Object[][] data = new Object[contracts.size()][7];
        	
        	for (int i=0; i<contracts.size(); i++){
  	
    			data[i][0] = "# " + contracts.get(i).getCode();
        		data[i][1] = contracts.get(i).getCustomerName();
        		data[i][2] = contracts.get(i).getCustomerTelNum();
        		data[i][3] = contracts.get(i).getActivationDate();
        		data[i][4] = "Calculate";
        		data[i][5] = "Calculate";
        		data[i][6] = "Update";
        		
        	}
        	JTable table = new JTable(data, columnNames);
        	
        	DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
        		private static final long serialVersionUID = 1L;

        		@Override
        	    public boolean isCellEditable(int row, int column) {
        			if (column==4||column==5||column==6){
        				return true;
        			}
        			else{
        				return false;
        			}
        	        
        	    }
        	};
        	table.setModel(tableModel);
        	table.getColumn("Statistics").setCellRenderer(new ButtonRenderer());
            table.getColumn("Statistics").setCellEditor(new ButtonEditorCU(new JCheckBox()));
            table.getColumn("Cost/Balance").setCellRenderer(new ButtonRenderer());
            table.getColumn("Cost/Balance").setCellEditor(new ButtonEditorCC(new JCheckBox()));
            table.getColumn("Free Data/Calls/Sms").setCellRenderer(new ButtonRenderer());
            table.getColumn("Free Data/Calls/Sms").setCellEditor(new ButtonEditorCR(new JCheckBox()));
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        	scrollPane = new JScrollPane(table);
        	table.setAlignmentX(Component.CENTER_ALIGNMENT);
        	
        	
        	table.addMouseListener(new MouseAdapter() {
        		  public void mouseClicked(MouseEvent e) {
        		      if (e.getClickCount() == 2) {
        		    	  
    	    		      JTable target = (JTable)e.getSource();
    	    		      int row = target.getSelectedRow();  
        		    	  try {
        		    		  int z=0;
        		    		  for (int i=0; i<contracts.size(); i++){
        		    			  if(String.valueOf(contracts.get(i).getCode()).equals(String.valueOf(target.getModel().getValueAt(row, 0)).substring(2))){
        		    				  z = i;
        		    			  }
        		    		  }
    						  JOptionPane.showMessageDialog(null, contracts.get(z).toString(), "Contract Details", JOptionPane.INFORMATION_MESSAGE);
    					  } 
        		    	  catch (HeadlessException e1) {
    						  e1.printStackTrace();
    					  } 
        		    	  catch (Exception e1) {
    						  e1.printStackTrace();
    					  }
    	    		      
        		      }
        		  }
        	});
        	
        	jp2.add(scrollPane, BorderLayout.CENTER);
    	}
    	return scrollPane;
    }

	//renders the buttons which are present inside JTable cells
    class ButtonRenderer extends JButton implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
    	    setOpaque(true);
    	  }

    	  public Component getTableCellRendererComponent(JTable table, Object value,
    	      boolean isSelected, boolean hasFocus, int row, int column) {
    	    if (isSelected) {
    	      setForeground(table.getSelectionForeground());
    	      setBackground(table.getSelectionBackground());
    	    } else {
    	      setForeground(table.getForeground());
    	      setBackground(UIManager.getColor("Button.background"));
    	    }
    	    setText((value == null) ? "" : value.toString());
    	    return this;
    	  }
    }

    //Button for "contract creation" in services tab
	class ButtonEditor extends DefaultCellEditor {
	
		private static final long serialVersionUID = 1L;
		protected JButton button;
	    private String emptyLabel;
	    private boolean isPushed;
	    private int row2;
	    private JTable table2;

    	public ButtonEditor(JCheckBox checkBox) {
    	    super(checkBox);
    	    button = new JButton();
    	    button.setOpaque(true);
    	    button.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	            fireEditingStopped();
    	        }
    	    });
    	}

    	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    		
    	    if (isSelected) {
    	      button.setForeground(table.getSelectionForeground());
    	      button.setBackground(table.getSelectionBackground());
    	    } else {
    	      button.setForeground(table.getForeground());
    	      button.setBackground(table.getBackground());
    	    }
    	    emptyLabel = (value == null) ? "" : value.toString();
    	    button.setText(emptyLabel);
    	    isPushed = true;
    	    row2 = row;
    	    table2 = table;
    	    return button;
    	}

    	public Object getCellEditorValue() {
    	    if (isPushed) {
    	 
    	        JTextField actdate = new JTextField(5);
    	        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	        Date date = new Date();
    	        actdate.setText(dateFormat.format(date));
    	        
    	        cname.setText(cname.getText());
    	        cnumber.setText(cnumber.getText());
    	        adddisc.setText(adddisc.getText());
    	        pmeth.setText(pmeth.getText());
    	         	        
    	        JPanel myPanel = new JPanel();
    	        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
    	        myPanel.add(new JLabel("Customer name (Name Surname):"));
    	        myPanel.add(cname);
    	        myPanel.add(Box.createHorizontalStrut(15)); 
    	        myPanel.add(new JLabel("Customer phone number (10 digits):"));
    	        myPanel.add(cnumber);
    	        myPanel.add(Box.createHorizontalStrut(15)); 
    	        myPanel.add(new JLabel("Activation date (dd/mm/yyyy):"));
    	        myPanel.add(actdate);
    	        myPanel.add(Box.createHorizontalStrut(15));
    	        myPanel.add(new JLabel("Payment method:"));
    	        myPanel.add(pmeth);
    	        myPanel.add(Box.createHorizontalStrut(15));
    	        myPanel.add(new JLabel("Extra discount as a decimal (e.g. 0.15=15%):"));
    	        myPanel.add(adddisc);
    	        
    	        
    	        boolean created = false;            //check if contract was successfully created
    	        int result=0;
    	        
				try {
					if(!contracts.isEmpty()){
						
						result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter Contract Fields", JOptionPane.OK_CANCEL_OPTION);
						
						if(result == JOptionPane.OK_OPTION&&!cname.getText().isEmpty()&&!cnumber.getText().isEmpty()
								&&!actdate.getText().isEmpty()&&!pmeth.getText().isEmpty()&&!adddisc.getText().isEmpty()){
							if(!cnameChecker(cname.getText()).equals(cname.getText())){
								JOptionPane.showMessageDialog(null, "Invalid name format.\n"
										+ "Please re-enter it as 'Name Surname'.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if (cnumberChecker(cnumber.getText()).equals("not10digits")){
								JOptionPane.showMessageDialog(null, "Telephone number needs to have exactly 10 digits.\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if (cnumberChecker(cnumber.getText()).equals("non-numeric")){
								JOptionPane.showMessageDialog(null, "Non-numeric values detected in telephone number.\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(actdateChecker(actdate.getText()).equals("Invalid")){
								JOptionPane.showMessageDialog(null, "Invalid date format. Type it again exactly as dd/mm/yyyy.\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(actdateChecker(actdate.getText()).equals("InvDay")){
								JOptionPane.showMessageDialog(null, ".\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(actdateChecker(actdate.getText()).equals("InvMonth")){
								JOptionPane.showMessageDialog(null, "Specified month is invalid.\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(actdateChecker(actdate.getText()).equals("outOfRange")){
								JOptionPane.showMessageDialog(null, "Specified year is out of range(2000-2020).\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(actdateChecker(actdate.getText()).equals("malformed")){
								JOptionPane.showMessageDialog(null, "Invalid date format (malformed).\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(actdateChecker(actdate.getText()).equals("non-numeric")){
								JOptionPane.showMessageDialog(null, "Invalid date format (d, m or y non-numeric).\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else if(adddiscChecker(adddisc.getText()).equals("non-numeric")){
								JOptionPane.showMessageDialog(null, "Invalid discount (non-numeric).\n"
										+ "Please try again.",
										"Alert", JOptionPane.OK_CANCEL_OPTION);
							}
							else{
								Contract ctc = new Contract(contracts.get(contracts.size()-1).getCode()+1,
										readServices.getService(String.valueOf(table2.getModel().getValueAt(1, 0)), 
												String.valueOf(row2+1)), cname.getText(), cnumber.getText(), 
										actdate.getText(), pmeth.getText(), Double.parseDouble(adddisc.getText()));
								contracts.add(ctc);
								created = true;
							}
							
						}
						else if(result == JOptionPane.OK_OPTION){
							JOptionPane.showMessageDialog(null, "Some of the fields were left empty.\nPlease try again",
									"Alert", JOptionPane.OK_CANCEL_OPTION);
						}
						
					}
					else{
						JOptionPane.showMessageDialog(null, "Contracts file needs to be loaded first.", "Alert", JOptionPane.OK_CANCEL_OPTION);
					}
					
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
    	        
    	        if (result == JOptionPane.OK_OPTION) {
    	        	if (created){
    	        		
    	        		JOptionPane.showMessageDialog(null, "Contract Created Successfully with code #" + contracts.get(contracts.size() - 1).getCode(),
    	        				"Result", JOptionPane.INFORMATION_MESSAGE);
    	        		cname.setText(null);
    	    	        cnumber.setText(null);
    	    	        actdate.setText(null);
    	    	        pmeth.setText(null);
    	    	        adddisc.setText(null);
    	        		
    	        	}
    	        }
    	        else{
    	        	JOptionPane.showMessageDialog(null, "Contract creation was canceled", "Alert", JOptionPane.OK_CANCEL_OPTION);
    	        }
    	        
    	    }
    	    isPushed = false;
    	    return new String(emptyLabel);
    	}

    	public boolean stopCellEditing() {
    	    isPushed = false;
    	    return super.stopCellEditing();
    	}

    	protected void fireEditingStopped() {
    	    super.fireEditingStopped();
    	}
	}

	//Button for "customer statistics" update in contracts tab
	class ButtonEditorCU extends DefaultCellEditor {
	
		private static final long serialVersionUID = 1L;
		protected JButton button;
	    private String emptyLabel;
	    private boolean isPushed;
	    private int row2;
	    private JTable table2;

    	public ButtonEditorCU(JCheckBox checkBox) {
    	    super(checkBox);
    	    button = new JButton();
    	    button.setOpaque(true);
    	    button.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	            fireEditingStopped();
    	        }
    	    });
    	}

    	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    		
    	    if (isSelected) {
    	      button.setForeground(table.getSelectionForeground());
    	      button.setBackground(table.getSelectionBackground());
    	    } else {
    	      button.setForeground(table.getForeground());
    	      button.setBackground(table.getBackground());
    	    }
    	    emptyLabel = (value == null) ? "" : value.toString();
    	    button.setText(emptyLabel);
    	    isPushed = true;
    	    row2 = row;
    	    table2 = table;
    	    return button;
    	}

    	public Object getCellEditorValue() {
    	    if (isPushed) {
    	    	
    	    	JPanel myPanel = new JPanel();
    	    	JTextField ttl = new JTextField(5);
	    		JTextField su = new JTextField(5);
	    		JTextField ttm = new JTextField(5);
	    		JTextField dc = new JTextField(5);
    	    	myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
    	    	
    	    	int z=0;
    	    	String s = String.valueOf(table2.getModel().getValueAt(row2, 0)).substring(2);
    	    	for (int i=0;i<contracts.size();i++){
    	    		if(Integer.parseInt(s)==(contracts.get(i).getCode())){
    	    			z = i;
    	    		}
    	    	}
    	    	
    	    	if(contracts.get(z).getTypeOfService() instanceof TelephoneService){
    	    		
    	    		myPanel.add(new JLabel("Give Talk Time to Landlines (in minutes):"));
    	    		ttl.setText(String.valueOf(contracts.get(z).getTalkTimeLand()));
        	        myPanel.add(ttl);
        	        myPanel.add(Box.createHorizontalStrut(15));
    	    		myPanel.add(new JLabel("Give Talk Time to mobile (in minutes):"));
    	    		ttm.setText(String.valueOf(contracts.get(z).getTalkTimeMobile()));
        	        myPanel.add(ttm);
        	        myPanel.add(Box.createHorizontalStrut(15));
    	    		myPanel.add(new JLabel("Give number of SMSs used:"));
    	    		su.setText(String.valueOf(contracts.get(z).getSmsUsage()));
        	        myPanel.add(su);
        	        int result = JOptionPane.showConfirmDialog(null, myPanel, "Update Contract #"+ s +" Statistics", JOptionPane.OK_CANCEL_OPTION);
        	        if (result == JOptionPane.OK_OPTION){
        	        	
        	        	contracts.get(z).setTalkTimeMobile(Integer.parseInt(ttm.getText()));
            	        contracts.get(z).setTalkTimeLand(Integer.parseInt(ttl.getText()));
            	        contracts.get(z).setSmsUsage(Integer.parseInt(su.getText()));
            	        JOptionPane.showMessageDialog(null, "Contract Updated Successfully with code #"+ s,"Result", JOptionPane.INFORMATION_MESSAGE);
        	        }
        	        else{
        	        	JOptionPane.showMessageDialog(null, "Contract #"+ s +" update was cancelled", "Canceled", JOptionPane.OK_CANCEL_OPTION);
        	        }
        	        
    	    	}
    	    	else{
    	    		
    	    		myPanel.add(new JLabel("Give Data consumed (in GB):"));
    	    		dc.setText(String.valueOf(contracts.get(z).getDataConsumed()));
        	        myPanel.add(dc);
        	        int result = JOptionPane.showConfirmDialog(null, myPanel, "Update Contract #"+ s +" Statistics", JOptionPane.OK_CANCEL_OPTION);
        	        if(result == JOptionPane.OK_OPTION){
        	        	
        	        	contracts.get(z).setDataConsumed(Integer.parseInt(dc.getText()));
        	        	JOptionPane.showMessageDialog(null, "Contract Updated Successfully with code #"+ s,"Result", JOptionPane.INFORMATION_MESSAGE);
        	        }
        	        else{
        	        	JOptionPane.showMessageDialog(null, "Contract #"+ s +" update was cancelled", "Canceled", JOptionPane.OK_CANCEL_OPTION);
        	        }
        	        
    	    	}


    	        ttl.setText(null);
    	        ttm.setText(null);
    	        su.setText(null);
    	        dc.setText(null);
    	    }
    	    isPushed = false;
    	    return new String(emptyLabel);
    	}

    	public boolean stopCellEditing() {
    	    isPushed = false;
    	    return super.stopCellEditing();
    	}

    	protected void fireEditingStopped() {
    	    super.fireEditingStopped();
    	}
	}

	//Button for "cost/available bance" calculation in contracts tab
	class ButtonEditorCC extends DefaultCellEditor {
	
		private static final long serialVersionUID = 1L;
		protected JButton button;
	    private String emptyLabel;
	    private boolean isPushed;
	    private int row2;
	    private JTable table2;

    	public ButtonEditorCC(JCheckBox checkBox) {
    	    super(checkBox);
    	    button = new JButton();
    	    button.setOpaque(true);
    	    button.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	            fireEditingStopped();
    	        }
    	    });
    	}

    	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    		
    	    if (isSelected) {
    	      button.setForeground(table.getSelectionForeground());
    	      button.setBackground(table.getSelectionBackground());
    	    } else {
    	      button.setForeground(table.getForeground());
    	      button.setBackground(table.getBackground());
    	    }
    	    emptyLabel = (value == null) ? "" : value.toString();
    	    button.setText(emptyLabel);
    	    isPushed = true;
    	    row2 = row;
    	    table2 = table;
    	    return button;
    	}

    	public Object getCellEditorValue() {
    	    if (isPushed) {
    	    	int z=0;
    	    	String s = String.valueOf(table2.getModel().getValueAt(row2, 0)).substring(2);
    	    	for (int i=0;i<contracts.size();i++){
    	    		if(Integer.parseInt(s)==(contracts.get(i).getCode())){
    	    			z =i;
    	    		}
    	    	}
    	    	System.out.println(z);
    	    	if(contracts.get(z).getTypeOfService() instanceof PrepaidTelephoneService){
    	    		JOptionPane.showMessageDialog
        	    	(null, "Contract with code #" + s + " has balance: " + contracts.get(z).calcCost()+"", "Balance Left", JOptionPane.INFORMATION_MESSAGE);
    	    	}
    	    	else{
    	    		JOptionPane.showMessageDialog
        	    	(null, "Contract with code #" + s + " has cost: " + contracts.get(z).calcCost()+"", "Monthly Cost", JOptionPane.INFORMATION_MESSAGE);
    	    	}
    	    	
    	    }
    	    isPushed = false;
    	    return new String(emptyLabel);
    	}

    	public boolean stopCellEditing() {
    	    isPushed = false;
    	    return super.stopCellEditing();
    	}

    	protected void fireEditingStopped() {
    	    super.fireEditingStopped();
    	}
	}

	//Button for "remaining calls/sms/data" calculation in contracts tab
	class ButtonEditorCR extends DefaultCellEditor {
		
		private static final long serialVersionUID = 1L;
		protected JButton button;
	    private String emptyLabel;
	    private boolean isPushed;
	    private int row2;
	    private JTable table2;

    	public ButtonEditorCR(JCheckBox checkBox) {
    	    super(checkBox);
    	    button = new JButton();
    	    button.setOpaque(true);
    	    button.addActionListener(new ActionListener() {
    	        public void actionPerformed(ActionEvent e) {
    	            fireEditingStopped();
    	        }
    	    });
    	}

    	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    		
    	    if (isSelected) {
    	      button.setForeground(table.getSelectionForeground());
    	      button.setBackground(table.getSelectionBackground());
    	    } else {
    	      button.setForeground(table.getForeground());
    	      button.setBackground(table.getBackground());
    	    }
    	    emptyLabel = (value == null) ? "" : value.toString();
    	    button.setText(emptyLabel);
    	    isPushed = true;
    	    row2 = row;
    	    table2 = table;
    	    return button;
    	}

    	public Object getCellEditorValue() {
    	    if (isPushed) {
    	    	int z=0;
    	    	String s = String.valueOf(table2.getModel().getValueAt(row2, 0)).substring(2);
    	    	for (int i=0;i<contracts.size();i++){
    	    		if(Integer.parseInt(s)==(contracts.get(i).getCode())){
    	    			z = i;
    	    		}
    	    	}
    	    	System.out.println(z);
    	    	if(contracts.get(z).getTypeOfService() instanceof TelephoneService){
    	    		JOptionPane.showMessageDialog
        	    	(null, "Contract with code #" + s + " has: " + contracts.get(z).calcRem()+" free calls and "
        	    			+ contracts.get(z).calcRemSms() + " free sms remaining!", "Balance Left", JOptionPane.INFORMATION_MESSAGE);
    	    	}
    	    	else{
    	    		JOptionPane.showMessageDialog
        	    	(null, "Contract with code #" + s + " has: " + contracts.get(z).calcRem()+
        	    			" free GB remaining!", "Monthly Cost", JOptionPane.INFORMATION_MESSAGE);
    	    	}
    	    	
    	    }
    	    isPushed = false;
    	    return new String(emptyLabel);
    	}

    	public boolean stopCellEditing() {
    	    isPushed = false;
    	    return super.stopCellEditing();
    	}

    	protected void fireEditingStopped() {
    	    super.fireEditingStopped();
    	}
	}
	
	//enables contracts tab functionality
	public void contractsTabFunctionality(){
		JLabel yLabel = new JLabel("Below are the current active contracts:");
		yLabel.setAlignmentX(CENTER_ALIGNMENT);
		yLabel.setFont(font);
		JLabel yLabel777 = new JLabel("double click for full details");
        yLabel777.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel eyLabel1 = new JLabel(" ");
		JLabel eyLabel2 = new JLabel(" ");
		JLabel eyLabel3 = new JLabel(" ");
		jp2.add(yLabel);
		jp2.add(yLabel777);
		jp2.add(eyLabel1);
		scroolAll = GUITableContractsAll(false, null);
		jp2.add(eyLabel2);
		JButton gotoType = new JButton("Go to type Selection");
		gotoType.setAlignmentX(Component.CENTER_ALIGNMENT);
		gotoType.setFont(font);
		jp2.add(gotoType);
		jp2.add(eyLabel3);
		gotoType.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				yLabel.setVisible(false);
				eyLabel1.setVisible(false);
				eyLabel2.setVisible(false);
				eyLabel3.setVisible(false);
				yLabel777.setVisible(false);
				gotoType.setVisible(false);
				GUITableContractsAll(true, scroolAll);
				
				JLabel label2 = new JLabel("Please choose one the following types of Services, with which to display any Contracts:");
        		label2.setAlignmentX(Component.CENTER_ALIGNMENT);
                label2.setFont(font);
                JLabel emptyLabel88 = new JLabel(" ");
                JLabel emptyLabel888 = new JLabel(" ");
                JLabel emptyLabel8888 = new JLabel(" ");
                JLabel emptyLabel88888 = new JLabel(" ");
                JLabel emptyLabel888888 = new JLabel(" ");
        		jp2.add(label2);
        		jp2.add(emptyLabel88);
        		JButton type1 = new JButton("Mobile Telephone Contract");
        		JButton type2 = new JButton("Prepaid Mobile");
        		JButton type3 = new JButton("Mobile Internet");
        		type1.setAlignmentX(Component.CENTER_ALIGNMENT);
        		type1.setFont(font);
        		jp2.add(type1);
        		jp2.add(emptyLabel888);
        		type2.setAlignmentX(Component.CENTER_ALIGNMENT);
        		type2.setFont(font);
        		jp2.add(type2);
        		jp2.add(emptyLabel8888);
        		type3.setAlignmentX(Component.CENTER_ALIGNMENT);
        		type3.setFont(font);
        		jp2.add(type3);
        		jp2.add(emptyLabel88888);
        		jp2.add(emptyLabel888888);
        		
        		JButton back2 = new JButton("Back to 'Diplay All'");
        		back2.setAlignmentX(Component.CENTER_ALIGNMENT);
        		jp2.add(back2);
        		back2.addActionListener(new ActionListener(){
        			public void actionPerformed(ActionEvent e){
        				emptyLabel88.setVisible(false);
        				emptyLabel888.setVisible(false);
        				emptyLabel8888.setVisible(false);
        				emptyLabel88888.setVisible(false);
        				emptyLabel888888.setVisible(false);
        				label2.setVisible(false);
        				type1.setVisible(false);
        				type2.setVisible(false);
        				type3.setVisible(false);
        				back2.setVisible(false);
        				contractsTabFunctionality();
        			}
        			
        		});
        		
        		
        		type1.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				emptyLabel88.setVisible(false);
        				emptyLabel888.setVisible(false);
        				emptyLabel8888.setVisible(false);
        				emptyLabel88888.setVisible(false);
        				emptyLabel888888.setVisible(false);
        				label2.setVisible(false);
        				type1.setVisible(false);
        				type2.setVisible(false);
        				type3.setVisible(false);
        				back2.setVisible(false);
        				JLabel emptyLabel11 = new JLabel("Active Contracts for type 'Telephone Contract'");
        				emptyLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
        				emptyLabel11.setFont(font);
    	                JLabel emptyLabel000 = new JLabel("double click for full details");
    	                emptyLabel000.setAlignmentX(Component.CENTER_ALIGNMENT);
    	                JLabel emptyLabel0000 = new JLabel(" ");
    	        		jp2.add(emptyLabel11);
    	        		jp2.add(emptyLabel000);
    	        		jp2.add(emptyLabel0000);
    	        		JScrollPane jemptyLabel = GUITableContracts("ContractTelephoneService", false, null);
    	        		JLabel emptyLabel00000 = new JLabel(" ");
    	        		jp2.add(emptyLabel00000);
    	        		JButton back = new JButton("Back to Type selection");
    	        		back.setAlignmentX(Component.CENTER_ALIGNMENT);
    	        		jp2.add(back);
    	        		JLabel emptyLabel000000 = new JLabel(" ");
    	        		jp2.add(emptyLabel000000);
    	        		back.addActionListener(new ActionListener(){
    	        			public void actionPerformed(ActionEvent e){
    	        				emptyLabel88.setVisible(true);
    	        				emptyLabel888.setVisible(true);
		        				emptyLabel8888.setVisible(true);
		        				emptyLabel88888.setVisible(true);
		        				emptyLabel888888.setVisible(true);
    	        				label2.setVisible(true);
    	        				type1.setVisible(true);
    	        				type2.setVisible(true);
    	        				type3.setVisible(true);
    	        				back2.setVisible(true);
    	        				emptyLabel11.setVisible(false);
    	        				emptyLabel000.setVisible(false);
    	        				emptyLabel0000.setVisible(false);
    	        				emptyLabel00000.setVisible(false);
    	        				emptyLabel000000.setVisible(false);
    	        				back.setVisible(false);
    	        				GUITableContracts("ContractTelephoneService", true, jemptyLabel);
    	        			}
    	        		});
        			}
        		});
        		type2.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				emptyLabel88.setVisible(false);
        				emptyLabel888.setVisible(false);
        				emptyLabel8888.setVisible(false);
        				emptyLabel88888.setVisible(false);
        				emptyLabel888888.setVisible(false);
        				label2.setVisible(false);
        				type1.setVisible(false);
        				type2.setVisible(false);
        				type3.setVisible(false);
        				back2.setVisible(false);
        				JLabel emptyLabel11 = new JLabel("Active Contracts for type 'Prepaid Telephone'");
        				emptyLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
        				emptyLabel11.setFont(font);
    	                JLabel emptyLabel000 = new JLabel("double click for full details");
    	                emptyLabel000.setAlignmentX(Component.CENTER_ALIGNMENT);
    	                JLabel emptyLabel0000 = new JLabel(" ");
    	        		jp2.add(emptyLabel11);
    	        		jp2.add(emptyLabel000);
    	        		jp2.add(emptyLabel0000);
    	        		JScrollPane jemptyLabel = GUITableContracts("PrepaidTelephoneService", false, null);
    	        		JLabel emptyLabel00000 = new JLabel(" ");
    	        		jp2.add(emptyLabel00000);
    	        		JButton back = new JButton("Back to Type selection");
    	        		back.setAlignmentX(Component.CENTER_ALIGNMENT);
    	        		jp2.add(back);
    	        		JLabel emptyLabel000000 = new JLabel(" ");
    	        		jp2.add(emptyLabel000000);
    	        		back.addActionListener(new ActionListener(){
    	        			public void actionPerformed(ActionEvent e){
    	        				emptyLabel88.setVisible(true);
    	        				emptyLabel888.setVisible(true);
		        				emptyLabel8888.setVisible(true);
		        				emptyLabel88888.setVisible(true);
		        				emptyLabel888888.setVisible(true);
    	        				label2.setVisible(true);
    	        				type1.setVisible(true);
    	        				type2.setVisible(true);
    	        				type3.setVisible(true);
    	        				back2.setVisible(true);
    	        				emptyLabel11.setVisible(false);
    	        				emptyLabel000.setVisible(false);
    	        				emptyLabel0000.setVisible(false);
    	        				emptyLabel00000.setVisible(false);
    	        				emptyLabel000000.setVisible(false);
    	        				back.setVisible(false);
    	        				GUITableContracts("PrepaidTelephoneService", true, jemptyLabel);
    	        			}
    	        		});
        			}
        		});
        		type3.addActionListener(new ActionListener() {
        			public void actionPerformed(ActionEvent e) {
        				emptyLabel88.setVisible(false);
        				emptyLabel888.setVisible(false);
        				emptyLabel8888.setVisible(false);
        				emptyLabel88888.setVisible(false);
        				emptyLabel888888.setVisible(false);
        				label2.setVisible(false);
        				type1.setVisible(false);
        				type2.setVisible(false);
        				type3.setVisible(false);
        				back2.setVisible(false);
        				JLabel emptyLabel11 = new JLabel("Active Contracts for type 'Mobile Internet'");
        				emptyLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
        				emptyLabel11.setFont(font);
    	                JLabel emptyLabel000 = new JLabel("double click for full details");
    	                emptyLabel000.setAlignmentX(Component.CENTER_ALIGNMENT);
    	                JLabel emptyLabel0000 = new JLabel(" ");
    	        		jp2.add(emptyLabel11);
    	        		jp2.add(emptyLabel000);
    	        		jp2.add(emptyLabel0000);
    	        		JScrollPane jemptyLabel = GUITableContracts("InternetService", false, null);
    	        		JLabel emptyLabel00000 = new JLabel(" ");
    	        		jp2.add(emptyLabel00000);
    	        		JButton back = new JButton("Back to Type selection");
    	        		back.setAlignmentX(Component.CENTER_ALIGNMENT);
    	        		jp2.add(back);
    	        		JLabel emptyLabel000000 = new JLabel(" ");
    	        		jp2.add(emptyLabel000000);
    	        		back.addActionListener(new ActionListener(){
    	        			public void actionPerformed(ActionEvent e){
    	        				emptyLabel88.setVisible(true);
    	        				emptyLabel888.setVisible(true);
		        				emptyLabel8888.setVisible(true);
		        				emptyLabel88888.setVisible(true);
		        				emptyLabel888888.setVisible(true);
    	        				label2.setVisible(true);
    	        				type1.setVisible(true);
    	        				type2.setVisible(true);
    	        				type3.setVisible(true);
    	        				back2.setVisible(true);
    	        				emptyLabel11.setVisible(false);
    	        				emptyLabel000.setVisible(false);
    	        				emptyLabel0000.setVisible(false);
    	        				emptyLabel00000.setVisible(false);
    	        				emptyLabel000000.setVisible(false);
    	        				back.setVisible(false);
    	        				GUITableContracts("InternetService", true, jemptyLabel);
    	        			}
    	        		});
    	        		
        			}
        		});
    		
			}
		});
	}
	
	//enables services tab functionality
	public void servicesTabFunctionality(){
		JLabel emptyLabel2 = new JLabel("Please choose one the following types of Services, with which to create a new Contract:");
		emptyLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        emptyLabel2.setFont(font);
        JLabel emptyLabel00 = new JLabel(" ");
        JLabel emptyLabel99 = new JLabel(" ");
        JLabel emptyLabel999 = new JLabel(" ");
		jp1.add(emptyLabel2);
		jp1.add(emptyLabel00);
		JButton type1 = new JButton("Mobile Telephone Contract");
		JButton type2 = new JButton("Prepaid Mobile");
		JButton type3 = new JButton("Mobile Internet");
		type1.setAlignmentX(Component.CENTER_ALIGNMENT);
		type1.setFont(font);
		type2.setAlignmentX(Component.CENTER_ALIGNMENT);
		type2.setFont(font);
		type3.setAlignmentX(Component.CENTER_ALIGNMENT);
		type3.setFont(font);
		jp1.add(type1);
		jp1.add(emptyLabel999);
		jp1.add(type2);
		jp1.add(emptyLabel99);
		jp1.add(type3);
		
		type1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emptyLabel00.setVisible(false);
				emptyLabel2.setVisible(false);
				type1.setVisible(false);
				type2.setVisible(false);
				type3.setVisible(false);
				JLabel emptyLabel11 = new JLabel("Available Services for type 'Telephone Contract'");
				emptyLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
				emptyLabel11.setFont(font);
                JLabel emptyLabel000 = new JLabel("double click for full details");
                emptyLabel000.setAlignmentX(Component.CENTER_ALIGNMENT);
                JLabel emptyLabel0000 = new JLabel(" ");
        		jp1.add(emptyLabel11);
        		jp1.add(emptyLabel000);
        		jp1.add(emptyLabel0000);
        		jemptyLabel = GUITableServices("ContractTelephoneService", false, null);
        		JLabel emptyLabel00000 = new JLabel(" ");
        		jp1.add(emptyLabel00000);
        		JButton back = new JButton("Back to Type selection");
        		back.setAlignmentX(Component.CENTER_ALIGNMENT);
        		jp1.add(back);
        		JLabel emptyLabel000000 = new JLabel(" ");
        		jp1.add(emptyLabel000000);
        		back.addActionListener(new ActionListener(){
        			public void actionPerformed(ActionEvent e){
        				emptyLabel00.setVisible(true);
        				emptyLabel2.setVisible(true);
        				type1.setVisible(true);
        				type2.setVisible(true);
        				type3.setVisible(true);
        				emptyLabel11.setVisible(false);
        				emptyLabel000.setVisible(false);
        				emptyLabel0000.setVisible(false);
        				emptyLabel00000.setVisible(false);
        				emptyLabel000000.setVisible(false);
        				back.setVisible(false);
        				GUITableServices("ContractTelephoneService", true, jemptyLabel);
        			}
        		});
			}
		});
		type2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emptyLabel00.setVisible(false);
				emptyLabel2.setVisible(false);
				type1.setVisible(false);
				type2.setVisible(false);
				type3.setVisible(false);
				JLabel emptyLabel11 = new JLabel("Available Services for type 'Prepaid Telephone'");
				emptyLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
				emptyLabel11.setFont(font);
                JLabel emptyLabel000 = new JLabel("double click for full details");
                emptyLabel000.setAlignmentX(Component.CENTER_ALIGNMENT);
                JLabel emptyLabel0000 = new JLabel(" ");
        		jp1.add(emptyLabel11);
        		jp1.add(emptyLabel000);
        		jp1.add(emptyLabel0000);
        		JScrollPane jemptyLabel = GUITableServices("PrepaidTelephoneService", false, null);
        		JLabel emptyLabel00000 = new JLabel(" ");
        		jp1.add(emptyLabel00000);
        		JButton back = new JButton("Back to Type selection");
        		back.setAlignmentX(Component.CENTER_ALIGNMENT);
        		jp1.add(back);
        		JLabel emptyLabel000000 = new JLabel(" ");
        		jp1.add(emptyLabel000000);
        		back.addActionListener(new ActionListener(){
        			public void actionPerformed(ActionEvent e){
        				emptyLabel00.setVisible(true);
        				emptyLabel2.setVisible(true);
        				type1.setVisible(true);
        				type2.setVisible(true);
        				type3.setVisible(true);
        				emptyLabel11.setVisible(false);
        				emptyLabel000.setVisible(false);
        				emptyLabel0000.setVisible(false);
        				emptyLabel00000.setVisible(false);
        				emptyLabel000000.setVisible(false);
        				back.setVisible(false);
        				GUITableServices("PrepaidTelephoneService", true, jemptyLabel);
        			}
        		});
			}
		});
		type3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				emptyLabel00.setVisible(false);
				emptyLabel2.setVisible(false);
				type1.setVisible(false);
				type2.setVisible(false);
				type3.setVisible(false);
				JLabel emptyLabel11 = new JLabel("Available Services for type 'Mobile Internet'");
				emptyLabel11.setAlignmentX(Component.CENTER_ALIGNMENT);
				emptyLabel11.setFont(font);
                JLabel emptyLabel000 = new JLabel("double click for full details");
                emptyLabel000.setAlignmentX(Component.CENTER_ALIGNMENT);
                JLabel emptyLabel0000 = new JLabel(" ");
        		jp1.add(emptyLabel11);
        		jp1.add(emptyLabel000);
        		jp1.add(emptyLabel0000);
        		JScrollPane jemptyLabel = GUITableServices("InternetService", false, null);
        		JLabel emptyLabel00000 = new JLabel(" ");
        		jp1.add(emptyLabel00000);
        		JButton back = new JButton("Back to Type selection");
        		back.setAlignmentX(Component.CENTER_ALIGNMENT);
        		jp1.add(back);
        		JLabel emptyLabel000000 = new JLabel(" ");
        		jp1.add(emptyLabel000000);
        		back.addActionListener(new ActionListener(){
        			public void actionPerformed(ActionEvent e){
        				emptyLabel00.setVisible(true);
        				emptyLabel2.setVisible(true);
        				type1.setVisible(true);
        				type2.setVisible(true);
        				type3.setVisible(true);
        				emptyLabel11.setVisible(false);
        				emptyLabel000.setVisible(false);
        				emptyLabel0000.setVisible(false);
        				emptyLabel00000.setVisible(false);
        				emptyLabel000000.setVisible(false);
        				back.setVisible(false);
        				GUITableServices("InternetService", true, jemptyLabel);
        			}
        		});
        		
			}
		});
	}
	
	//checks the validity
	public String cnameChecker(String cname){
		boolean good = false;
		while(!good){
			
			int space = 0;
			int length = cname.length();
			for(int i=0; i<length; i++){
				if(cname.substring(i, i+1).equals(" ")){
					space++;
				}
			}
			if(space>1 || space==0){
				System.out.println();
				System.err.println("Invalid name format. Please re-enter it as 'Name Surname':");
				cname="wrong";
				break;
			}
			else{
				good = true;
			}
		}
		return cname;
	}
	
	//checks the validilty of 'Customer Number' field at contract creation
	public String cnumberChecker(String cnumber){
		boolean good = false;
		boolean notNumeric = false;
		while(!good){
			int length = cnumber.length();
			for(int i=0; i<length; i++){
				try{
					Integer.parseInt(cnumber.substring(i, i+1));
				}
				catch(NumberFormatException nfe){
					notNumeric = true;
				}
			}
			if (length != 10){
				System.out.println();
				System.err.println("Telephone number needs to have exactly 10 digits. Please try again:");
				cnumber = "not10digits";
				break;
			}
			else if(notNumeric){
				System.out.println();
				System.err.println("Non-numeric values detected. Please try again:");
				cnumber = "non-numeric";
				break;
			}
			else{
				good=true;
			}
			notNumeric = false;
		}
		return cnumber;
		
	}
	
	//checks the validilty of 'Activation Date' field at contract creation
	public String actdateChecker(String actdate){
		boolean good = false;
		while(!good){
			
			try{
				String first = actdate.substring(2, 3);
				String second = actdate.substring(5, 6);
				int dd = Integer.parseInt(actdate.substring(0, 2));
				int mm = Integer.parseInt(actdate.substring(3, 5));
				int yyyy = Integer.parseInt(actdate.substring(6));
				
				int length = actdate.length();
				if(!first.equals("/") || !second.equals("/") || length !=10){
					System.out.println();
					System.err.println("Invalid date format. Type it again exactly in this (dd/mm/yyyy) format:");
					actdate = "Invalid";
					break;
				}
				else if(dd<1 || dd>31){
					System.out.println();
					System.err.println("Specified day is invalid. Try again:");
					actdate = "invDay";
					break;
				}
				else if(mm<1 || mm>12){
					System.out.println();
					System.err.println("Specified month is invalid. Try again:");
					actdate = "invMonth";
					break;
				}
				else if(yyyy<2000 || yyyy>2020){
					System.out.println();
					System.err.println("Specified year is out of range(2000-2020). Try again:");
					actdate = "outOfRange";
					break;
				}
				else{
					good=true;
				}
			}
			catch(StringIndexOutOfBoundsException sioobe){
				System.out.println();
				System.err.println("Invalid date format (malformed). Type it again exactly in this (dd/mm/yyyy) format:");
				actdate = "malformed";
			}
			catch(NumberFormatException nfe){
				System.out.println();
				System.err.println("Invalid date format (d, m or y non-numeric). Type it again exactly in this (dd/mm/yyyy) format:");
				actdate = "non-numeric";
			}
			
		}
		return actdate;
	}
	
	//checks the validilty of 'Additional Discount' field at contract creation
	public String adddiscChecker(String adddisc){
		boolean notNumeric = false;
		try{
			Double.parseDouble(adddisc);
		}
		catch(NumberFormatException nfe){
			notNumeric = true;
		}
		if(notNumeric){
			System.err.println("Additiona Discount was non-numeric.\nPlease try again.");
			adddisc="non-numeric";
		}
		return adddisc;
	}
	
	//run it
    public static void main (String []args){
        new mainApp();
    }
}
