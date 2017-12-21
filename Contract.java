
public class Contract {

	private int code;
	private Service typeOfService;
	private String customerName;
	private String customerTelNum;
	private String activationDate;
	private String paymentMethod;
	private double additionalDiscount;
	
	//Stats//
	private int talkTimeLand;
	private int talkTimeMobile;
	private int smsUsage;
	private double dataConsumed;
	
	//Constructor for Telephone Service with stats
	public Contract(Service typeOfService, String customerName, String customerTelNum, String activationDate,
			String paymentMethod, double additionalDiscount, int talkTimeLand, int talkTimeMobile, int smsUsage) {
		//this.code = code;
		this.typeOfService = typeOfService;
		this.customerName = customerName;
		this.customerTelNum = customerTelNum;
		this.activationDate = activationDate;
		this.paymentMethod = paymentMethod;
		this.additionalDiscount = additionalDiscount;
		this.talkTimeLand = talkTimeLand;
		this.talkTimeMobile = talkTimeMobile;
		this.smsUsage = smsUsage;
	}
	
	//Constructor for Internet Service with stats
	public Contract(Service typeOfService, String customerName, String customerTelNum, String activationDate,
			String paymentMethod, double additionalDiscount, double dataConsumed) {
		//this.code = code;
		this.typeOfService = typeOfService;
		this.customerName = customerName;
		this.customerTelNum = customerTelNum;
		this.activationDate = activationDate;
		this.paymentMethod = paymentMethod;
		this.additionalDiscount = additionalDiscount;
		this.dataConsumed = dataConsumed;
	}
	
	//Constructor for Service with no stats
	public Contract(int code, Service typeOfService, String customerName, String customerTelNum, String activationDate,
			String paymentMethod, double additionalDiscount) {
		this.code = code;
		this.typeOfService = typeOfService;
		this.customerName = customerName;
		this.customerTelNum = customerTelNum;
		this.activationDate = activationDate;
		this.paymentMethod = paymentMethod;
		this.additionalDiscount = additionalDiscount;
	}
	
	//Constructor with only code initialization 
	public Contract(){

	}
	//-----------Getters------------//
	
	public int getCode() {
		return code;
	}
	public Service getTypeOfService() {
		return typeOfService;
	}
	public String getCustomerName() {
		return customerName;
	}
	public String getCustomerTelNum() {
		return customerTelNum;
	}
	public String getActivationDate() {
		return activationDate;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public double getAdditionalDiscount() {
		return additionalDiscount;
	}
	public int getTalkTimeLand() {
		return talkTimeLand;
	}
	public int getTalkTimeMobile() {
		return talkTimeMobile;
	}
	public int getSmsUsage() {
		return smsUsage;
	}
	public double getDataConsumed() {
		return dataConsumed;
	}

	public void setCode(int code) {
		this.code = code;
	}

	//-----------Setters------------//
	
	public void setTypeOfService(Service typeOfService) {
		this.typeOfService = typeOfService;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setCustomerTelNum(String customerTelNum) {
		this.customerTelNum = customerTelNum;
	}

	public void setActivationDate(String activationDate) {
		this.activationDate = activationDate;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setAdditionalDiscount(double additionalDiscount) {
		this.additionalDiscount = additionalDiscount;
	}

	public void setTalkTimeLand(int talkTimeLand) {
		this.talkTimeLand = talkTimeLand;
	}

	public void setTalkTimeMobile(int talkTimeMobile) {
		this.talkTimeMobile = talkTimeMobile;
	}

	public void setSmsUsage(int smsUsage) {
		this.smsUsage = smsUsage;
	}

	public void setDataConsumed(double dataConsumed) {
		this.dataConsumed = dataConsumed;
	}

	@Override
	public String toString() {
		if(typeOfService instanceof TelephoneService){
			
			return "Contract with code: " + code +"\n" + "- typeOfService: " + typeOfService+"\n" + "- customerName: " + customerName+"\n"
					+ "- customerTelNum: " + customerTelNum+"\n" + "- activationDate: " + activationDate+"\n" + "- paymentMethod: "
					+ paymentMethod+"\n" + "- additionalDiscount: " + additionalDiscount*100 + "%" +"\n"+"- talkTimeLand: " + talkTimeLand
					+ " minutes"+"\n" +"- talkTimeMobile: " + talkTimeMobile + " minutes"+"\n" +"- smsUsage: " + smsUsage + " SMS";
			
		}
		else{
			
			return "Contract with code: " + code +"\n" + "- typeOfService: " + typeOfService+"\n" + "- customerName: " + customerName+"\n"
					+ "- customerTelNum: " + customerTelNum+"\n" + "- activationDate: " + activationDate+"\n" + "- paymentMethod: "
					+ paymentMethod+"\n" + "- additionalDiscount: " + additionalDiscount*100 + "%"+"\n" + "- dataConsumed: " + dataConsumed
					+ "GB";
			
		}

	}
	
	//calculate cost/available balance of contract
	public double calcCost(){
		
		double cost=0;
		double balance=0;
		if(typeOfService instanceof ContractTelephoneService){
			if (talkTimeLand+talkTimeMobile>((TelephoneService) typeOfService).getFreeCalls() || smsUsage>((TelephoneService) typeOfService).getFreeSMS()){
				if (talkTimeLand+talkTimeMobile >((TelephoneService) typeOfService).getFreeCalls()){
					cost = ((talkTimeLand+talkTimeMobile) - ((TelephoneService) typeOfService).getFreeCalls())*
							((TelephoneService) typeOfService).getCallCharge()/100;                                   //cost of extra minutes
				}
				if (smsUsage>((TelephoneService) typeOfService).getFreeSMS()){
					cost = cost + smsUsage - ((TelephoneService) typeOfService).getFreeSMS()*
							((TelephoneService) typeOfService).getSMSCharge()/100;                                    //cost of extra sms
				}
				
				cost = cost + typeOfService.getMonthlyCharge();                                                       //cost with fixed monthly cost
				cost = cost - cost*(additionalDiscount+ ((ContractTelephoneService) typeOfService).getDiscount());    //final cost with discounts accounted
			}
			else{
				cost = typeOfService.getMonthlyCharge();                                                              //fixed monthly cost
				cost = cost - cost*(additionalDiscount+((ContractTelephoneService) typeOfService).getDiscount());     //final cost with discounts accounted
			}
			return cost;
		}
		else if(typeOfService instanceof InternetService){
			if (dataConsumed>((InternetService) typeOfService).getFreeData()){
				cost = (dataConsumed - ((InternetService) typeOfService).getFreeData())*
						((InternetService) typeOfService).getDataCharge()*1000/100;                          //cost of extra data
				cost = cost + typeOfService.getMonthlyCharge();                                              //cost with fixed monthly cost
				cost = cost - cost*(additionalDiscount+((InternetService) typeOfService).getDiscount());     //final cost with discounts accounted
			}
			else{
				cost = typeOfService.getMonthlyCharge(); //fixed monthly cost
				cost = cost - cost*(additionalDiscount+((InternetService) typeOfService).getDiscount());     //final cost with discounts accounted 
			}
			return cost;
		}
		else if(typeOfService instanceof PrepaidTelephoneService){
			if (talkTimeLand+talkTimeMobile>((TelephoneService) typeOfService).getFreeCalls() || 
					smsUsage>((TelephoneService) typeOfService).getFreeSMS()){
				if (talkTimeLand+talkTimeMobile>((TelephoneService) typeOfService).getFreeCalls()){
					balance = ((talkTimeLand+talkTimeMobile) - ((TelephoneService) typeOfService).getFreeCalls())*
							((TelephoneService) typeOfService).getCallCharge()/100;                                     //cost of extra minutes
				}
				if (smsUsage>((TelephoneService) typeOfService).getFreeSMS()){
					balance = balance + smsUsage - ((TelephoneService) typeOfService).getFreeSMS()*
							((TelephoneService) typeOfService).getSMSCharge()/100;                                      //extra sms cost
				}
				balance = ((PrepaidTelephoneService) typeOfService).getAvailableBalance() - balance;                    //calculate remaining balance
			}
			else{
				balance = ((PrepaidTelephoneService) typeOfService).getAvailableBalance();        				//balance without any out of program cost
			}
			return balance;
		}
		else{
			return cost;
		}
		
		
	}
	
	//calculate remaining sms calls/data
	public double calcRem(){
		double cr=0, dr=0;
		if(typeOfService instanceof TelephoneService){
			
			if(((TelephoneService) typeOfService).getFreeCalls()<talkTimeLand + talkTimeMobile){
				cr=0;
			}
			else{
				cr=((TelephoneService) typeOfService).getFreeCalls() - talkTimeLand + talkTimeMobile;
			}
			return cr;
		}
		else{
			if(((InternetService) typeOfService).getFreeData()<dataConsumed){
				dr=0;
			}
			else{
				dr=((InternetService) typeOfService).getFreeData() - dataConsumed;
			}
			return dr;
		}
		
	}
	
	//calculate remaining sms
	public double calcRemSms(){
		double sr=0;
		if(((TelephoneService) typeOfService).getFreeSMS()<smsUsage){
			sr=0;
		}
		else{
			sr=((TelephoneService) typeOfService).getFreeSMS() - smsUsage;
		}
		return sr;
	}
	
	//-----------Info about fields---------//
	
	//code:                 automatically incremented int number
	//typeofservice:        object of type "Service", points to whichever service the contract contains.
	//customerTelNum:       10 digits customer number
	//activationDate:       date in dd/mm/yyy format
	//paymentmethod:        cash, online etc
	//additionalDiscount:   extra discount presented as a double var
	//talkTimeLand:         in minutes
	//talkTimeMobile:       in minutes
	//smsUsage:             in #SMS
	//dataConsumed:         in GB
	
	/* ------Formula for monthly cost/available balance calculation------
	 * 
	 * 
	 * -- Prepaid:
	 * RemainingAvailableBalance = ExtraCallsCost + ExtraSMSCost - CurrentAvailableBalance
	 * 
	 * -- Telephone Contract:
	 * Cost = (ExtraCallsCost + ExtraSMSCost + FixedMonthlyCost) * (1 - (DefaultServiceDiscount + ExtraDiscount))
	 * 
	 * -- Internet:
	 * Cost = (ExtraDataCost + FixedMonthlyCost) * (1 - (DefaultServiceDiscount + ExtraDiscount))
	 * 
	 */
}
