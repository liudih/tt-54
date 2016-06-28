package com.rabbit.dto.category;

import java.io.Serializable;
import java.util.Date;

public class AttributeValue implements Serializable {

	private static final long serialVersionUID = 1L;

	Integer ivalueid;
	Integer ikeyid;
	String ccreateuser;
	Date dcreatedate;

	public Integer getIvalueid() {
		return ivalueid;
	}

	public void setIvalueid(Integer ivalueid) {
		this.ivalueid = ivalueid;
	}

	public Integer getIkeyid() {
		return ikeyid;
	}

	public void setIkeyid(Integer ikeyid) {
		this.ikeyid = ikeyid;
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
