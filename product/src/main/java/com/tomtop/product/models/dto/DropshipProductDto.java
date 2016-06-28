package com.tomtop.product.models.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import com.tomtop.product.utils.DateFormatUtils;

public class DropshipProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2702390785364341069L;

	private Integer iid;

	private String cemail;

	private String csku;

	private Boolean bstate;

	private Date dcreatedate;

	private Date dupdatestatedate;

	private Integer iwebsiteid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public Boolean getBstate() {
		return bstate;
	}

	public void setBstate(Boolean bstate) {
		this.bstate = bstate;
	}

	public Date getDcreatedate() {
		if (dcreatedate == null) {
			try {
				dcreatedate = DateFormatUtils.getCurrentUtcTimeDate();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Date getDupdatestatedate() {
		return dupdatestatedate;
	}

	public void setDupdatestatedate(Date dupdatestatedate) {
		this.dupdatestatedate = dupdatestatedate;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
}
