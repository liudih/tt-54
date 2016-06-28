package com.tomtop.product.models.dto;

public class ProductAttributeDto {

	
	private String clistingid;
	private String csku;
	private Integer ikeyid;
	private Integer ivalueid;
	private String ckeyname;
	private String cvaluename;
	
	public String getClistingid() {
		return clistingid;
	}
	public void setClistingid(String clistingid) {
		this.clistingid = clistingid;
	}
	public String getCsku() {
		return csku;
	}
	public void setCsku(String csku) {
		this.csku = csku;
	}
	public Integer getIkeyid() {
		return ikeyid;
	}
	public void setIkeyid(Integer ikeyid) {
		this.ikeyid = ikeyid;
	}
	public Integer getIvalueid() {
		return ivalueid;
	}
	public void setIvalueid(Integer ivalueid) {
		this.ivalueid = ivalueid;
	}
	public String getCkeyname() {
		return ckeyname;
	}
	public void setCkeyname(String ckeyname) {
		this.ckeyname = ckeyname;
	}
	public String getCvaluename() {
		return cvaluename;
	}
	public void setCvaluename(String cvaluename) {
		this.cvaluename = cvaluename;
	}
}
