package enums;

public enum OrderLableEnum {
	DROP_SHIPPING("DropShipping"), TOTAL_ORDER("TotalOrder");

	private String name;

	private OrderLableEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
