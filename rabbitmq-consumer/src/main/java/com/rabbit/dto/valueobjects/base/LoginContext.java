package com.rabbit.dto.valueobjects.base;

import java.io.Serializable;

public class LoginContext implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8632142753906799616L;

	String memberID;
	int groupID;
	Serializable payload;
	String ltc;
	String stc;
	String currencyCode;
	String countryCode;

	LoginContext(String ltc, String stc, String memberID, int groupID,
			Serializable payload) {
		super();
		this.ltc = ltc;
		this.stc = stc;
		this.memberID = memberID;
		this.groupID = groupID;
		this.payload = payload;
	}

	public LoginContext() {
		super();
	}

	public String getMemberID() {
		return memberID;
	}

	public int getGroupID() {
		return groupID;
	}

	public Serializable getPayload() {
		return payload;
	}

	public boolean isLogin() {
		return (memberID != null);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Long Term Cookie
	 *
	 * @see CoookieTrackingFilter
	 * @return
	 */
	public String getLTC() {
		return ltc;
	}

	/**
	 * Short Term Cookie
	 *
	 * @return
	 */
	public String getSTC() {
		return stc;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public String toString() {
		return "LoginContext [memberID=" + memberID + ", groupID=" + groupID
				+ ", payload=" + payload + ", ltc=" + ltc + ", stc=" + stc
				+ "]";
	}

}
