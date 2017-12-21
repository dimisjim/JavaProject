
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ContractReadWrite {
	
	private ArrayList <Contract> contracts = new ArrayList<Contract>();
	
	public void loadfile(String contractsfilepath, String servicesfilepath){
		
		int counter = 0;

		File f = null;
		BufferedReader reader = null;
		String line;

		try{
			f = new File(contractsfilepath);
		}
		catch (NullPointerException e){
			System.err.println ("File not found.");
		}

		try{
			reader = new BufferedReader(new FileReader(f));
		}
		catch (FileNotFoundException e ){
			System.err.println("Error opening file!");
		}

		try	{
			
			Contract contract = null;
			
			//Mandatory fields
			String type = null, name = null, customer = null, actdate = null;
			String pnumber = null;
			int codenum = 0;
			
			//default fields if not specified in tag
			String pmethod = null;
			double adddisc = 0, datac=0;
			int ttl=0, ttm=0, smsu=0;
			
			boolean foundInitialTag = false;
			boolean hasContractTag = false;
			
			//get contents from services.txt
			ServiceRead sr = new ServiceRead();
			sr.loadfile(servicesfilepath);
			ArrayList<Service> sl = new ArrayList<Service>();
			sl = sr.getList();
			
			line = reader.readLine();
			while (line!=null){
				
				if(line.trim().equals("CONTRACT_LIST") || line.trim().equals("contract_list")){
					foundInitialTag = true;
					line = reader.readLine();
					while (line!=null){
						
						if(line.trim().equals("{")){
							line = reader.readLine();
							while (line!=null){
								
								if(line.trim().equals("CONTRACT") || line.trim().equals("contract")){
									hasContractTag = true;
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
											else if(line.trim().startsWith("CUSTOMER") || line.trim().startsWith("customer")){
												
												customer = st.nextToken().substring(1);
												while (st.hasMoreTokens()){ 
													customer = customer.concat(" ");
													customer = customer.concat(st.nextToken());
												}
												customer = customer.substring(0, customer.length() -1);
											}
											else if(line.trim().startsWith("TYPE") || line.trim().startsWith("type")){
												type = st.nextToken();
											}
											else if(line.trim().startsWith("PHONE_NUMBER") || line.trim().startsWith("phone_number")){
												pnumber = st.nextToken().substring(1);
												while (st.hasMoreTokens()){ 
													pnumber = pnumber.concat(" ");
													pnumber = pnumber.concat(st.nextToken());
												}
												pnumber = pnumber.substring(0, pnumber.length() -1);
											}
											else if(line.trim().startsWith("ACTIVATION_DATE") || line.trim().startsWith("activation_date")){
												
												actdate = st.nextToken().substring(1);
												while (st.hasMoreTokens()){ 
													actdate = actdate.concat(" ");
													actdate = actdate.concat(st.nextToken());
												}
												actdate = actdate.substring(0, actdate.length() -1);
											}
											else if(line.trim().startsWith("ADD_DISCOUNT") || line.trim().startsWith("add_discount")){
												adddisc = Double.parseDouble(st.nextToken());
											}
											else if(line.trim().startsWith("PAYMENT_METHOD") || line.trim().startsWith("payment_method")){
												pmethod = st.nextToken().substring(1);
												while (st.hasMoreTokens()){ 
													pmethod = pmethod.concat(" ");
													pmethod = pmethod.concat(st.nextToken());
												}
												pmethod = pmethod.substring(0, pmethod.length() -1);
											}
											else if(line.trim().startsWith("MONTHLY_USAGE") ||line.trim().startsWith("monthly_usage")){
												line = reader.readLine();
												if(line.trim().equals("{")){
													line = reader.readLine();
													while(!line.trim().equals("}")){
														
														StringTokenizer stt = new StringTokenizer(line.trim());
														stt.nextToken();
														if(line.trim().startsWith("FIXED") || line.trim().startsWith("fixed")){
															ttl = Integer.parseInt(stt.nextToken());
														}
														else if(line.trim().startsWith("MOBILE") || line.trim().startsWith("mobile")){
															ttm = Integer.parseInt(stt.nextToken());
														}
														else if(line.trim().startsWith("SMS") || line.trim().startsWith("sms")){
															smsu = Integer.parseInt(stt.nextToken());
														}
														else if(line.trim().startsWith("DATA") || line.trim().startsWith("data")){
															datac = Double.parseDouble(stt.nextToken());
														}
														
														line = reader.readLine();
													}
													
												}
											}
											else if(line.trim().startsWith("CONTRACT_NUMBER") ||line.trim().startsWith("contract_number")){
												codenum = Integer.parseInt(st.nextToken());
											}
											line = reader.readLine();
										
										} //check all tags inside CONTRACT tag, loop
										counter++;
										
										if ((pnumber!=null) && (name!=null) && (customer!=null) && (actdate!=null) && (type!=null) && (codenum!=0)){ //check if tag contains type, name, price
											
											if (type.equals("InternetService")){
												for (int i=0; i <sl.size();i++){
													if (sl.get(i).getName().equals(name)){
														contract = new Contract();
														contract.setTypeOfService(sl.get(i));
														contract.setCustomerName(customer);
														contract.setActivationDate(actdate);
														contract.setPaymentMethod(pmethod);
														contract.setAdditionalDiscount(adddisc);
														contract.setCustomerTelNum(pnumber);
														contract.setDataConsumed(datac);
														contract.setCode(codenum);
														contracts.add(contract);
													}
													else if(i==sl.size()){
														System.err.println("Given name: '" + name + "'  does not match any existing service");
														break;
													}
												}
											}
											else if (type.equals("ContractTelephoneService")){
												for (int i=0; i <sl.size();i++){
													if (sl.get(i).getName().equals(name)){
														contract = new Contract();
														contract.setTypeOfService(sl.get(i));
														contract.setCustomerName(customer);
														contract.setActivationDate(actdate);
														contract.setPaymentMethod(pmethod);
														contract.setAdditionalDiscount(adddisc);
														contract.setCustomerTelNum(pnumber);
														contract.setTalkTimeLand(ttl);
														contract.setTalkTimeMobile(ttm);
														contract.setSmsUsage(smsu);
														contract.setCode(codenum);
														contracts.add(contract);
													}
													else if(i==sl.size()){
														System.err.println("Given name: '" + name + "'  does not match any existing service");
														break;
													}
												}
											}
											else if (type.equals("PrepaidTelephoneService")){
												for (int i=0; i <sl.size();i++){
													if (sl.get(i).getName().equals(name)){
														contract = new Contract();
														contract.setTypeOfService(sl.get(i));
														contract.setCustomerName(customer);
														contract.setActivationDate(actdate);
														contract.setPaymentMethod(pmethod);
														contract.setAdditionalDiscount(adddisc);
														contract.setCustomerTelNum(pnumber);
														contract.setTalkTimeLand(ttl);
														contract.setTalkTimeMobile(ttm);
														contract.setSmsUsage(smsu);
														contract.setCode(codenum);
														contracts.add(contract);
													}
													else if(i==sl.size()){
														System.err.println("Given name: '" + name + "'  does not match any existing service");
														break;
													}
												}
											}
										}
										else if(name==null){
											System.err.println("Contract tag #" + counter + " does not contain 'SERVICE_NAME' field.\n");
										}
										else if(customer==null){
											System.err.println("Contract tag #" + counter + " does not contain 'CUSTOMER' field.\n");
										}
										else if(type==null){
											System.err.println("Contract tag #" + counter + " does not contain 'TYPE' field.\n");
										}
										else if(actdate==null){
											System.err.println("Contract tag #" + counter + " does not contain 'ACTIVATION_DATE' field.\n");
										}
										else if(pnumber==null){
											System.err.println("Contract tag #" + counter + " does not contain 'PHONE_NUMBER' field.\n");
										}
										else if(codenum==0){
											System.err.println("Contract tag #" + counter + " does not contain 'CONTRACT_NUMBER' field.\n");
										}
										
										//re-initialization of var values
										type = null;
										name = null; 
										customer = null; 
										actdate = null;
										pnumber = null;
										codenum = 0;
										
										datac=0;
										ttl=0;
										ttm=0;
										smsu=0;
										
										line = reader.readLine();
										
									} //if ({ is found)
									else{
										line = reader.readLine();
									}
									
								} //if (CONTRACT is found)
								else{
									line = reader.readLine();
								}
								
							} //main while loop (inside CONTRACT_LIST)
							if (!hasContractTag){
								System.err.println("There are no individual 'CONTRACT' tags inside 'CONTRACT_LIST' tag");
							}
							
						} //first {
						else{
							line = reader.readLine();
						}
						
					} //loop until first bracket is found
					
				} //if (CONTRACT_LIST is found)
				else{
					line = reader.readLine();
				}

			} //loop until CONTRACT_LIST is found
			if (!foundInitialTag){
				System.err.println("Initial 'CONTRACT_LIST' tag was not found");
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
	
	public boolean writefile(String data, ArrayList<Contract> contracts){

		File f = null;
		PrintWriter writer = null;

		try{
			f = new File(data);
		}
		catch (NullPointerException e){
			System.err.println ("File not found.");
		}

		try{
			writer = new PrintWriter(f, "UTF-8");
		}
		catch (FileNotFoundException e ){
			System.err.println("Error opening file!");
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		writer.println("CONTRACT_LIST");
		writer.println("{");
		
		for (int i=0; i <contracts.size();i++){
			
			writer.println("\t"+"CONTRACT");
			writer.println("\t"+"{");
			writer.println("\t"+"\t"+"CONTRACT_NUMBER "+contracts.get(i).getCode());
			writer.println("\t"+"\t"+"TYPE "+String.valueOf(contracts.get(i).getTypeOfService().getClass()).substring(6));
			writer.println("\t"+"\t"+"SERVICE_NAME "+"\""+contracts.get(i).getTypeOfService().getName()+"\"");
			writer.println("\t"+"\t"+"CUSTOMER "+"\""+contracts.get(i).getCustomerName()+"\"");
			writer.println("\t"+"\t"+"PHONE_NUMBER "+"\""+contracts.get(i).getCustomerTelNum()+"\"");
			writer.println("\t"+"\t"+"ACTIVATION_DATE "+"\""+contracts.get(i).getActivationDate()+"\"");
			writer.println("\t"+"\t"+"PAYMENT_METHOD "+"\""+contracts.get(i).getPaymentMethod()+"\"");
			writer.println("\t"+"\t"+"ADD_DISCOUNT "+contracts.get(i).getAdditionalDiscount());
			writer.println("\t"+"\t"+"MONTHLY_USAGE");
			writer.println("\t"+"\t"+"{");
			if(contracts.get(i).getTypeOfService() instanceof TelephoneService){
				
				writer.println("\t"+"\t"+"\t"+"FIXED "+contracts.get(i).getTalkTimeLand());
				writer.println("\t"+"\t"+"\t"+"MOBILE "+contracts.get(i).getTalkTimeMobile());
				writer.println("\t"+"\t"+"\t"+"SMS "+contracts.get(i).getSmsUsage());
			}
			else if (contracts.get(i).getTypeOfService() instanceof InternetService){

				writer.println("\t"+"\t"+"\t"+"DATA "+contracts.get(i).getDataConsumed());
			}
			writer.println("\t"+"\t"+"}");
			writer.println("\t"+"}");
			
		}
		writer.println("}");
		writer.close();
		return true;
	}
	
	// returns the list where contracts are saved
	public ArrayList<Contract> getList(){
		return contracts;
	}
	
}
