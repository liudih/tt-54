package com.tomtop.product.models.dto.base;

import java.io.Serializable;

public class Country implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	String cname;
	String cshortname;
	Integer ilanguageid;
	String ccurrency;
	Integer idefaultstorage;
	Boolean bshow;
	Integer ishowindex;

	String languageName;// 语言名称

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
		this.cname = cname;
	}

	public String getCshortname() {
		return cshortname;
	}

	public void setCshortname(String cshortname) {
		this.cshortname = cshortname;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCcurrency() {
		return ccurrency;
	}

	public void setCcurrency(String ccurrency) {
		this.ccurrency = ccurrency;
	}

	public Integer getIdefaultstorage() {
		return idefaultstorage;
	}

	public void setIdefaultstorage(Integer idefaultstorage) {
		this.idefaultstorage = idefaultstorage;
	}

	public Boolean getBshow() {
		return bshow;
	}

	public void setBshow(Boolean bshow) {
		this.bshow = bshow;
	}

	public Integer getIshowindex() {
		return ishowindex;
	}

	public void setIshowindex(Integer ishowindex) {
		this.ishowindex = ishowindex;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

}
