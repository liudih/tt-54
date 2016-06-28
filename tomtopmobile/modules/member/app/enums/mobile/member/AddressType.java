package enums.mobile.member;

/**
 * 订单地址枚举类型
 * 
 * @author lijun
 *
 */
public enum AddressType {
	SHIPPING(1, "SHIPPING", "SHIPPING"), BILLING(2, "Billing", "Billing");
	private int code;
	private String describeEN;
	private String describeCN;

	private AddressType(int code, String describeEN, String describeCN) {
		this.code = code;
		this.describeEN = describeEN;
		this.describeCN = describeCN;
	}

	public int getCode() {
		return code;
	}

	public String getDescribeEN() {
		return describeEN;
	}

	public String getDescribeCN() {
		return describeCN;
	}

	public static AddressType getType(int code) {
		AddressType[] values = AddressType.values();
		for (AddressType t : values) {
			if (t.getCode() == code) {
				return t;
			}
		}
		return null;
	}
}
