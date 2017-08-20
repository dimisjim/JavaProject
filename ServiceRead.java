//DIMITRIS MORAITIDIS, 3100240
//NIKI PAPAGORA, 3120140

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServiceRead {
	
	private ArrayList <Service> services = new ArrayList<Service>();
	
	public void loadfile(String data){
		
		int counter = 0;

		File f = null;
		BufferedReader reader = null;
		String line;

		try{
			f = new File(data);
		}
		catch (NullPointerException e){
			System.err.println ("File not found.");
		}

		try{
			reader = new BufferedReader(new FileReader(f));
		}
		catch (FileNotFoundException e){
			System.err.println("Error opening file!");
		}

		try	{
			
			//Mandatory fields
			Service service = null;
			String name = null;
			int price = 0;
			
			//default fields if not specified in tag
			int freeCalls = 300, freeSMS = 300, callCharge = 20, sMSCharge = 20, availableBalance = 50;
			double freeData = 2, dataCharge = 0.5;
			
			boolean hastype = false; //tag hastype checker
			
			boolean foundInitialTag = false;
			boolean hasServiceTag = false;
			
			line = reader.readLine();
			while (line!=null){
				
				if(line.trim().equals("SERVICE_LIST") || line.trim().equals("service_list")){
					foundInitialTag = true;
					line = reader.readLine();
					while (line!=null){
						
						if(line.trim().equals("{")){
							line = reader.readLine();
							while (line!=null){
								
								if(line.trim().equals("SERVICE") || line.trim().equals("service")){
									hasServiceTag = true;
									line = reader.readLine();
										
									if(line.trim().equals("{")){
										line = reader.readLine();
										while(!line.trim().equals("}")){
											
											StringTokenizer st = new StringTokenizer(line.trim());
											st.nextToken();
											
											if(line.trim().startsWith("SERVICE_NAME") || line.trim().startsWith("service_name")){
												
												name = st.nextToken().substring(1);
												while (st.hasMoreTokens()){ 
													name = name.concat(" ");
													name = name.concat(st.nextToken());
												}
												name = name.substring(0, name.length() -1);
												
											}
											else if(line.trim().startsWith("TYPE") || line.trim().startsWith("type")){
												if(line.trim().substring(5).trim().equals("InternetService")){
													service = new InternetService();
													hastype=true;
												}
												else if(line.trim().substring(5).trim().equals("ContractTelephoneService")){
													service = new ContractTelephoneService();
													hastype=true;
												}
												else if(line.trim().substring(5).trim().equals("PrepaidTelephoneService")){
													service = new PrepaidTelephoneService();
													hastype=true;
												}
											}
											else if(line.trim().startsWith("MONTHLY_PRICE") || line.trim().startsWith("monthly_price")){
												price = Integer.parseInt(st.nextToken());
											}
											else if(line.trim().startsWith("FREE_DATA") || line.trim().startsWith("free_data")){
												freeData = Double.parseDouble(st.nextToken());
											}
											else if(line.trim().startsWith("DATA_CHARGE") || line.trim().startsWith("data_charge")){
												dataCharge = Double.parseDouble(st.nextToken());
											}
											else if(line.trim().startsWith("FREE_CALLS") || line.trim().startsWith("free_calls")){
												freeCalls = Integer.parseInt(st.nextToken());
											}
											else if(line.trim().startsWith("FREE_SMS") || line.trim().startsWith("free_sms")){
												freeSMS = Integer.parseInt(st.nextToken());
											}
											else if(line.trim().startsWith("CALL_CHARGE") || line.trim().startsWith("call_charge")){
												callCharge = Integer.parseInt(st.nextToken());
											}
											else if(line.trim().startsWith("SMS_CHARGE") || line.trim().startsWith("sms_charge")){
												sMSCharge = Integer.parseInt(st.nextToken());
											}
											else if(line.trim().startsWith("AVAILABLE_BALANCE") || line.trim().startsWith("available_balance")){
												availableBalance = Integer.parseInt(st.nextToken());
											}
											
											line = reader.readLine();
										
										} //check all tags inside SERVICE tag, loop
										counter++;
										
										if ((hastype) && (price!=0) && (name!=null)){ //check if tag contains type, name, price
											
											if (service instanceof InternetService){
												service.setName(name);
												service.setMonthlyCharge(price);
												((InternetService) service).setFreeData(freeData);
												((InternetService) service).setDataCharge(dataCharge);
												services.add(service);
											}
											else if (service instanceof ContractTelephoneService){
												service.setName(name);
												service.setMonthlyCharge(price);
												((ContractTelephoneService) service).setCallCharge(callCharge);
												((ContractTelephoneService) service).setFreeCalls(freeCalls);
												((ContractTelephoneService) service).setFreeSMS(freeSMS);
												((ContractTelephoneService) service).setSMSCharge(sMSCharge);
												services.add(service);
											}
											else if (service instanceof PrepaidTelephoneService){
												service.setName(name);
												service.setMonthlyCharge(price);
												((PrepaidTelephoneService) service).setCallCharge(callCharge);
												((PrepaidTelephoneService) service).setFreeCalls(freeCalls);
												((PrepaidTelephoneService) service).setFreeSMS(freeSMS);
												((PrepaidTelephoneService) service).setSMSCharge(sMSCharge);
												((PrepaidTelephoneService) service).setAvailableBalance(availableBalance);
												services.add(service);
											}
										}
										else if(name==null){
											System.err.println("Service tag #" + counter + " does not contain 'SERVICE_NAME' field.\n");
										}
										else if(price==0){
											System.err.println("Service tag #" + counter + " does not contain 'MONTHLY_PRICE' field.\n");
										}
										else if(!hastype){
											System.err.println("Service tag #" + counter + " does not contain 'TYPE' field.\n");
										}
										
										//re-initialization of vars to default values
										price=0;
										name = null;
										hastype=false;
										
										freeCalls = 300;
										freeSMS = 300; 
										callCharge = 20; 
										sMSCharge = 20; 
										availableBalance = 50;
										freeData = 2; 
										dataCharge = 0.5;
										line = reader.readLine();
	
									} //if ({ is found)
									else{
										line = reader.readLine();
									}
									
								} //if (SERVICE is found)
								else{
									line = reader.readLine();
								}
								
							} //main while loop (inside SERVICE_LIST)
							if (!hasServiceTag){
								System.err.println("There are no individual 'SERVICE' tags inside 'SERVICE_LIST' tag");
							}
							
						} //first {
						else{
							line = reader.readLine();
						}
						
					} //loop until first bracket is found
					
				} //if (SERVICE_LIST is found)
				else{
					line = reader.readLine();
				}

			} //loop until SERVICE_LIST is found
			if (!foundInitialTag){
				System.err.println("Initial 'SERVICE_LIST' tag was not found");
			}
			
		} //try
		catch (IOException e){
			System.err.println("Error reading line " + counter + ".");
		}

		try {
			reader.close();
		}
		catch (IOException e){
			System.err.println("Error closing file.");
		}
		
	}
	
	/*
	 * x=1 print PrepaidTelephoneService
	 * x=2 print ContractTelephoneService
	 * x=3 print InternetService
	 * x=4 print all
	 */
	public void print(int x){
		int y=0;
		if(x==4){
			for (int i=0; i <services.size();i++){
				System.out.println();
				System.out.println(i+1+". " + services.get(i).toString());
			}
		}
		else if(x==1){
			for (int i=0; i <services.size();i++){
				if(services.get(i) instanceof PrepaidTelephoneService){
					System.out.println();
					System.out.println(y+1+". " + services.get(i).toString());
					y++;
				}
			}
			y=0;
		}
		else if(x==2){
			for (int i=0; i <services.size();i++){
				if(services.get(i) instanceof ContractTelephoneService){
					System.out.println();
					System.out.println(y+1+". " + services.get(i).toString());
					y++;
				}
			}
			y=0;
		}
		else if(x==3){
			for (int i=0; i <services.size();i++){
				if(services.get(i) instanceof InternetService){
					System.out.println();
					System.out.println(y+1+". " + services.get(i).toString());
					y++;
				}
			}
			y=0;
		}
		else{
			System.err.println("Wrong input argument");
		}

	}
	
	// Get Service depending on user's input (associates "choice" var from keyboard to specific service)
	public Service getService(String service, String choice) throws Exception{
		Service s = null;
		int y=0;
		boolean entered = false;
		for (int i=0; i <services.size();i++){
			String si = String.valueOf(services.get(i).getClass()).substring(6);
			if(service.equals(si)){
				y++;
				if(Integer.parseInt(choice)==y){
					s= services.get(i);
					entered = true;
					break;
				}
			}
		}
		if (!entered){
			throw new Exception();
		}
		return s;
	}
	
	// used by ContractReadWrite to save new typeofservice contract field 
	public ArrayList<Service> getList(){
		return services;
	}

}
