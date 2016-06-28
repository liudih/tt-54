package com.tomtop.product.models.dto.base;

public class NewArrival {

	private Integer ilanguage;
	private Integer iwebsiteid;
	private Integer icategoryid;
	private String clistingid;

	public Integer getIlanguage() {
		return ilanguage;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setIlanguage(Integer ilanguage) {
		this.ilanguage = ilanguage;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}
}
