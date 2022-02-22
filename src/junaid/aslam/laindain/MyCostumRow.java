package junaid.aslam.laindain;

public class MyCostumRow {
	
	private String name;
	private String amount;
	private char amountType;
	private String date;
	
	public MyCostumRow(String name, String amount, String date) {
		this.name= name;
		this.amount= amount;
		this.date= date;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(double amnt, char t) {
		if(t=='p')
			this.amount = String.valueOf('+'+amnt);
		else
			this.amount = String.valueOf('-'+amnt);
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date= date;
	}
	String datetostr(int d, int m, int y) {
		return (d+"/"+m+"/"+y);
	}

	public char getAmountType() {
		return amountType;
	}

	public void setAmountType(char amountType) {
		this.amountType = amountType;
	}
}