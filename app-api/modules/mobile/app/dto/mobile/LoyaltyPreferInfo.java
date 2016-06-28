package dto.mobile;

public class LoyaltyPreferInfo {

	// 优惠的金额,应用成功返回的为负数
	private Double value;

	private String code;

	// 优惠类型：coupon,推广码,积分
	private String preferType;

	// 根据自己需要记录额外信息
	private String extra;

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPreferType() {
		return preferType;
	}

	public void setPreferType(String preferType) {
		this.preferType = preferType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

}
