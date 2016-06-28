package com.rabbit.dto.product;

import java.io.Serializable;
import java.util.Date;

public class InventoryHistory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String creference;

	private String clistingid;

	private Integer iqty;

	private Integer iwebsiteid;

	private Boolean benabled;

	private Date dcreatedate;

	private Integer ibeforechangeqty;

	private Integer iafterchangeqty;

	private String ctype;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCreference() {
		return creference;
	}

	public void setCreference(String creference) {
		this.creference = creference;
	}

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Boolean getBenabled() {
		return benabled;
	}

	public void setBenabled(Boolean benabled) {
		this.benabled = benabled;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIbeforechangeqty() {
		return ibeforechangeqty;
	}

	public void setIbeforechangeqty(Integer ibeforechangeqty) {
		this.ibeforechangeqty = ibeforechangeqty;
	}

	public Integer getIafterchangeqty() {
		return iafterchangeqty;
	}

	public void setIafterchangeqty(Integer iafterchangeqty) {
		this.iafterchangeqty = iafterchangeqty;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

}