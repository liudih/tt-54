package com.tomtop.product.models.dto;

import java.text.ParseException;
import java.util.Date;

import com.tomtop.product.utils.DateFormatUtils;

public class ReportErrorDto {
	
    private Integer iid;

    private String clistingid;

    private String csku;

    private String cerrortype;

    private String cemail;

    private String cinquiry;

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
		this.clistingid = clistingid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public String getCerrortype() {
		return cerrortype;
	}

	public void setCerrortype(String cerrortype) {
		this.cerrortype = cerrortype;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public String getCinquiry() {
		return cinquiry;
	}

	public void setCinquiry(String cinquiry) {
		this.cinquiry = cinquiry;
	}

	public Date getDcreatedate() {
		if(dcreatedate == null){
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
    
}
