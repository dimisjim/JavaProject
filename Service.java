
public class Service {
	private String name;
	private int monthlyCharge;
	
	public Service() {
		super();
	}

	public Service(String name, int monthlyCharge) {
		this.name = name;
		this.monthlyCharge = monthlyCharge;
	}
	
	//-----------Getters------------//
	
	public String getName() {
		return name;
	}
	public int getMonthlyCharge() {
		return monthlyCharge;
	}
	
	//-----------Setters------------//
	
	public void setName(String name) {
		this.name = name;
	}

	public void setMonthlyCharge(int monthlyCharge) {
		this.monthlyCharge = monthlyCharge;
	}

	@Override
	public String toString() {
		return "Service with name: " + name + "\n" + "- monthlyCharge: " + monthlyCharge + "€";
	}
	
	//-----------Info about fields---------//
	
	//monthly charge: in €
	
}
