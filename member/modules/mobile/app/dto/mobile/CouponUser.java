package dto.mobile;

import utils.ValidataUtils;

public class CouponUser {

	
	private String code;
	private String desc;
	
	
	public String getCode() {
		return ValidataUtils.validataStr(code);
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return ValidataUtils.validataStr(desc);
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
