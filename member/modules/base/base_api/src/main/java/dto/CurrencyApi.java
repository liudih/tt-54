package dto;

import java.io.Serializable;

public class CurrencyApi implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String appkey;
	String sign;
	String format;
	String scur;
	String tcur;
	
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getScur() {
		return scur;
	}
	public void setScur(String scur) {
		this.scur = scur;
	}
	public String getTcur() {
		return tcur;
	}
	public void setTcur(String tcur) {
		this.tcur = tcur;
	}
}
