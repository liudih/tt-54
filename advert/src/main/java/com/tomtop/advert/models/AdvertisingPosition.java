package com.tomtop.advert.models;

import java.util.Date;

public class AdvertisingPosition {

	private Long iid;

	private Long ipositonid;

	private String cpositonname;

	private String ccreateuser;

	private Date dcreatedate;

	private String clastupdateduser;

	private Date dlastupdateddate;

	public Long getIid() {
		return iid;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}

	public Long getIpositonid() {
		return ipositonid;
	}

	public void setIpositonid(Long ipositonid) {
		this.ipositonid = ipositonid;
	}

	public String getCpositonname() {
		return cpositonname;
	}

	public void setCpositonname(String cpositonname) {
		this.cpositonname = cpositonname;
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

}
