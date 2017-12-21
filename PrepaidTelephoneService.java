

public class PrepaidTelephoneService extends TelephoneService {
	private int availableBalance;
	private final double discount = 0.25;
	
	
	public PrepaidTelephoneService() {
		super();
	}

	public PrepaidTelephoneService(String name, int monthlyCharge, int freeCalls, int freeSMS, int callCharge, int sMSCharge, int availableBalance) {
		super(name, monthlyCharge, freeCalls, freeSMS, callCharge, sMSCharge);
		this.availableBalance = availableBalance;
	}

	//-----------Getters------------//
	
	public int getAvailableBalance() {
		return availableBalance;
	}

	public double getDiscount() {
		return discount;
	}
	
	//-----------Setters------------//
	
	public void setAvailableBalance(int availableBalance) {
		this.availableBalance = availableBalance;
	}

	@Override
	public String toString() {
		return "PrepaidTelephoneService with name: " + getName() + "\n" + "- monthlyCharge: "
				+ getMonthlyCharge() + "€"+ "\n"+"- freeCalls: " + getFreeCalls() + " minutes"+ "\n"+"- freeSMS: " + getFreeSMS()
				+ " SMS"+ "\n"+"- callCharge: " + getCallCharge()*0.01 + "€/minute"+ "\n"+"- sMSCharge: " 
				+ getSMSCharge()*0.01 + "€/SMS"+ "\n"+"- availableBalance: " 
				+ availableBalance + "€"+ "\n"+"- discount: " + discount*100 + "%";
	}
	
	//-----------Info about fields---------//
	
	//availablebalance:  in €
	//discount:          percentage presented as a double var
	
}
