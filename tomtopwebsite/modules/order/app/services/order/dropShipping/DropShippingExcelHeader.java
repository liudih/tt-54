package services.order.dropShipping;

public enum DropShippingExcelHeader {
	DS_ORDER_NO("DS_ORDER_NO"), DS_ORDER_DATE("DS_ORDER_DATE"), SKU("SKU"), QTY(
			"QTY"), COUNTRY("COUNTRY"), NAME("NAME"), ADDRESS("ADDRESS"), CITY(
			"CITY"), STATE("STATE"), ZIPCODE("ZIPCODE"), PHONE("PHONE"), NOTE(
			"NOTE");

	private String name;

	DropShippingExcelHeader(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
