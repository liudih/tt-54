package valueobjects.tracking;

public enum CommissionType {
	Pending(10), 
	Processing(20),
	Success(30),
	Fail(0);

	private int type;

	private CommissionType(int type) {
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
