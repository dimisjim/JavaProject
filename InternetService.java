
public class InternetService extends Service {
	private double freeData, dataCharge;
	private final double discount = 0.3;
	
	public InternetService() {
		super();
	}

	public InternetService(String name, int monthlyCharge, int freeData, double dataCharge) {
		super(name, monthlyCharge);
		this.freeData = freeData;
		this.dataCharge = dataCharge;
	}

	//-----------Getters------------//

	public double getFreeData() {
		return freeData;
	}

	public double getDataCharge() {
		return dataCharge;
	}
	
	public double getDiscount() {
		return discount;
	}
	
	//-----------Setters------------//
	
	public void setFreeData(double freeData) {
		this.freeData = freeData;
	}

	public void setDataCharge(double dataCharge) {
		this.dataCharge = dataCharge;
	}

	@Override
	public String toString() {
		return "InternetService with name: " + getName() +"\n" + "- monthlyCharge: " + 
				getMonthlyCharge() + "€"+"\n"+"- freeData: " + freeData + "GB"+"\n"+"- dataCharge: " 
				+ dataCharge*0.01 + "€/MB"+"\n"+"- discount: " + discount*100 + "%";
	}
	
	//-----------Info about fields---------//
	
	//freedata:       in GB
	//data charge:    in cents/MB
	//discount:       percentage presented as a double var
	
}
