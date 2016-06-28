package com.tomtop.product.models.dto;

import java.text.ParseException;
import java.util.Date;

import com.tomtop.product.utils.DateFormatUtils;

public class WholesaleInquiryDto {
	
    private Integer iid;

    private String clistingid;

    private String csku;

    private String cname;

    private String cphone;

    private String cemail;

    private Double ftargetprice;

    private Integer iquantity;

    private String ccountrystate;

    private String ccompany;

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

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCphone() {
		return cphone;
	}

	public void setCphone(String cphone) {
		this.cphone = cphone;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Double getFtargetprice() {
		return ftargetprice;
	}

	public void setFtargetprice(Double ftargetprice) {
		this.ftargetprice = ftargetprice;
	}

	public Integer getIquantity() {
		return iquantity;
	}

	public void setIquantity(Integer iquantity) {
		this.iquantity = iquantity;
	}

	public String getCcountrystate() {
		return ccountrystate;
	}

	public void setCcountrystate(String ccountrystate) {
		this.ccountrystate = ccountrystate;
	}

	public String getCcompany() {
		return ccompany;
	}

	public void setCcompany(String ccompany) {
		this.ccompany = ccompany;
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
