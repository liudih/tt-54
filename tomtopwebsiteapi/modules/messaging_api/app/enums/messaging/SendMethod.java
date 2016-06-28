package enums.messaging;

/**
 * 
 * @author lijun
 *
 */
public enum SendMethod {
	SYSTEM("systeam send", "系统发送", 1), MANUAL("manual send", "人工发送", 2);

	private String describeEN;
	private String describeCH;
	private int code;

	private SendMethod(String describeEN, String describeCH, int code) {
		this.describeEN = describeEN;
		this.describeCH = describeCH;
		this.code = code;
	}

	public String getDescribeEN() {
		return describeEN;
	}

	public void setDescribeEN(String describeEN) {
		this.describeEN = describeEN;
	}

	public String getDescribeCH() {
		return describeCH;
	}

	public void setDescribeCH(String describeCH) {
		this.describeCH = describeCH;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public static SendMethod getSendMethod(int code) {
		SendMethod[] values = SendMethod.values();
		for (SendMethod v : values) {
			if (v.getCode() == code) {
				return v;
			}
		}
		return null;
	}
}
