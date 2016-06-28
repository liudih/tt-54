package com.rabbit.dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductCategoryRank implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String clistingid;

	private String csku;

	private Integer icategoryid;

	private Integer iwebsiteid;

	private Integer isales;

	private Date dcreatedate;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku == null ? null : csku.trim();
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIsales() {
		return isales!=null?isales:0;
//		return isales;
	}

	public void setIsales(Integer isales) {
		this.isales = isales;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}
}