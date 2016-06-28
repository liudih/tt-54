package valueobjects.tracking;

public enum CommissionOrderStatus {
	Unpaid(10), 
	Processing(20),
	Paid(30),
	Delete(40);
	
	private int type;

	private CommissionOrderStatus(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return String.valueOf(this.type);
	}
}
