package com.rabbit.dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductMultiattributeBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private Integer iwebsiteid;

	private String cparentsku;

	private String ccreateuser;

	private Date dcreatedate;

	private String clastupdateuser;

	private Date dlastupdatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCparentsku() {
		return cparentsku;
	}

	public void setCparentsku(String cparentsku) {
		this.cparentsku = cparentsku == null ? null : cparentsku.trim();
	}

	public String getCcreateuser() {
		return ccreateuser;
	}

	public void setCcreateuser(String ccreateuser) {
		this.ccreateuser = ccreateuser == null ? null : ccreateuser.trim();
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getClastupdateuser() {
		return clastupdateuser;
	}

	public void setClastupdateuser(String clastupdateuser) {
		this.clastupdateuser = clastupdateuser == null ? null : clastupdateuser
				.trim();
	}

	public Date getDlastupdatedate() {
		return dlastupdatedate;
	}

	public void setDlastupdatedate(Date dlastupdatedate) {
		this.dlastupdatedate = dlastupdatedate;
	}
}