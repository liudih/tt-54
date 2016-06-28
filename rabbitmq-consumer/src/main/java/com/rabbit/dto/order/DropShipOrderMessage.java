package com.rabbit.dto.order;

import java.io.Serializable;

public class DropShipOrderMessage extends Order implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2607362438008235699L;

	private String useremail;
	
	private String userorderid;
	
	private String cdropshippingid;

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getUserorderid() {
		return userorderid;
	}

	public void setUserorderid(String userorderid) {
		this.userorderid = userorderid;
	}

	public String getCdropshippingid() {
		return cdropshippingid;
	}

	public void setCdropshippingid(String cdropshippingid) {
		this.cdropshippingid = cdropshippingid;
	}
	
}
