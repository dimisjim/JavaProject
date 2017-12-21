

public class TelephoneService extends Service {
	private int freeCalls, freeSMS, callCharge, sMSCharge;
	
	
	public TelephoneService() {
		super();
	}

	public TelephoneService(String name, int monthlyCharge, int freeCalls, int freeSMS, int callCharge, int sMSCharge) {
		super(name, monthlyCharge);
		this.freeCalls = freeCalls;
		this.freeSMS = freeSMS;
		this.callCharge = callCharge;
		this.sMSCharge = sMSCharge;
	}
	
	//-----------Getters------------//
	
	public int getFreeCalls() {
		return freeCalls;
	}

	public int getSMSCharge() {
		return sMSCharge;
	}

	public int getFreeSMS() {
		return freeSMS;
	}

	public int getCallCharge() {
		return callCharge;
	}

	//-----------Setters------------//
	
	public void setFreeCalls(int freeCalls) {
		this.freeCalls = freeCalls;
	}
	
	public void setCallCharge(int callCharge) {
		this.callCharge = callCharge;
	}

	public void setFreeSMS(int freeSMS) {
		this.freeSMS = freeSMS;
	}

	public void setSMSCharge(int sMSCharge) {
		this.sMSCharge = sMSCharge;
	}

	@Override
	public String toString() {
		return "TelephoneService with name: " + getName()+ "\n" + "- monthlyCharge: "
				+ getMonthlyCharge() + "€"+ "\n"+"- freeCalls: " + freeCalls + " minutes"+ "\n"+"- freeSMS: " + freeSMS
				+ " SMS"+ "\n"+"- callCharge: " + callCharge*0.01 + "€/minute"+ "\n"+", sMSCharge: " + sMSCharge*0.01 + "€/SMS";
	}
	
	//-----------Info about fields---------//
	
	//freecalls:         in minutes
	//freesms:           in # of SMS
	//callcharge:        in cents/minute
	//smscharge:         in cents/sms
	
}
