package com.tomtop.product.models.dto;

import java.io.Serializable;
import java.util.Date;

public class ProductCategoryMapperDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String clistingid;

	private String csku;

	private Integer icategory;

	private String ccreateuser;

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

	public Integer getIcategory() {
		return icategory;
	}

	public void setIcategory(Integer icategory) {
		this.icategory = icategory;
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

	@Override
	public String toString() {
		return "ProductCategoryMapperDto [iid=" + iid + ", clistingid="
				+ clistingid + ", csku=" + csku + ", icategory=" + icategory
				+ ", ccreateuser=" + ccreateuser + ", dcreatedate="
				+ dcreatedate + "]";
	}

}