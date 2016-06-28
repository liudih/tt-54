package com.rabbit.dto.base;

import java.util.Date;

public class LanguageLocal {
	private Integer iid;

	private String cname;

	private String ccreateuser;

	private Date dcreatedate;

	private boolean bfallback;
	
	private String cfullname;
	

	public String getCfullname() {
		return cfullname;
	}

	public void setCfullname(String cfullname) {
		this.cfullname = cfullname;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}

	public boolean isBfallback() {
		return bfallback;
	}

	public void setBfallback(boolean bfallback) {
		this.bfallback = bfallback;
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
}