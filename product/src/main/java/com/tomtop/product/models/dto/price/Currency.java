package com.tomtop.product.models.dto.price;

import java.io.Serializable;
import java.util.Date;

public class Currency implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	String ccode;
	String csymbol;
	Double fexchangerate;
	String ccreateuser;
	Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	/**
	 * Example Currency Code: "USD"
	 * 
	 * @return
	 */
	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	/**
	 * Example Currency Symbol: "$"
	 * 
	 * @return
	 */
	public String getCsymbol() {
		return csymbol;
	}

	public void setCsymbol(String csymbol) {
		this.csymbol = csymbol;
	}

	public Double getFexchangerate() {
		return fexchangerate;
	}

	public void setFexchangerate(Double fexchangerate) {
		this.fexchangerate = fexchangerate;
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

}
