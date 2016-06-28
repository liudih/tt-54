package com.tomtop.product.models.dto.category;

public class CategoryLable {

	private Integer icategoryid;
	private String cname;
	private Integer ilevel;
	private Integer iparentid;
	private String cpath;
	
	public Integer getIcategoryid() {
		return icategoryid;
	}
	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Integer getIlevel() {
		return ilevel;
	}
	public void setIlevel(Integer ilevel) {
		this.ilevel = ilevel;
	}
	public Integer getIparentid() {
		return iparentid;
	}
	public void setIparentid(Integer iparentid) {
		this.iparentid = iparentid;
	}
	public String getCpath() {
		return cpath;
	}
	public void setCpath(String cpath) {
		this.cpath = cpath;
	} 
}
