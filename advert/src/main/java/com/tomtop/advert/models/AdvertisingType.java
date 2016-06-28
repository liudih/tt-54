package com.tomtop.advert.models;

import java.util.Date;

public class AdvertisingType {

	private Long iid;

	private Long iadvertisingid;

	private String cadvertisingname;

	private String ccreateuser;

	private Date dcreatedate;

	private String clastupdateduser;

	private Date dlastupdateddate;

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

	public String getClastupdateduser() {
		return clastupdateduser;
	}

	public void setClastupdateduser(String clastupdateduser) {
		this.clastupdateduser = clastupdateduser;
	}

	public Date getDlastupdateddate() {
		return dlastupdateddate;
	}

	public void setDlastupdateddate(Date dlastupdateddate) {
		this.dlastupdateddate = dlastupdateddate;
	}

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

	public Long getIadvertisingid() {
		return iadvertisingid;
	}

	public void setIadvertisingid(Long iadvertisingid) {
		this.iadvertisingid = iadvertisingid;
	}

	public String getCadvertisingname() {
		return cadvertisingname;
	}

	public void setCadvertisingname(String cadvertisingname) {
		this.cadvertisingname = cadvertisingname;
	}
}