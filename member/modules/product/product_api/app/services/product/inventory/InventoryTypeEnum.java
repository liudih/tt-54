package services.product.inventory;

public enum InventoryTypeEnum {
	SALE("sale"), INIT("init"), CANCEL_ORDER("cancel_order"), UPDATE("update");

	private String value;

	InventoryTypeEnum(String type) {
		this.value = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
