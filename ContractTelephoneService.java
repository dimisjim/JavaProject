
public class ContractTelephoneService extends TelephoneService{
	private final double discount = 0.2;
	
	public ContractTelephoneService(){
		super();
	}
	
	public ContractTelephoneService(String name, int monthlyCharge, int freeCalls, int freeSMS, int callCharge, int sMSCharge) {
		super(name, monthlyCharge, freeCalls, freeSMS, callCharge, sMSCharge);
	}

	//-----------Getters------------//
	
	public double getDiscount() {
		return discount;
	}

	@Override
	public String toString() {
		return "ContractTelephoneService with name: " + getName()+"\n" + "- monthlyCharge: "
				+ getMonthlyCharge() + "€"+"\n" +"- freeCalls: " + getFreeCalls() + " minutes"+"\n"+"- freeSMS: " + getFreeSMS()
				+ " SMS"+"\n"+"- callCharge: " + getCallCharge()*0.01 + "€/minute"+"\n"+"- sMSCharge: " + getSMSCharge()*0.01 + "€/SMS"+"\n"
				+"- discount: " + discount*100 + "%";
	}
	
	//-----------Info about fields---------//
	
	//discount:     percentage presented as a double var
	
}
