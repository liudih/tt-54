package com.tomtop.product.models.dto.wholesale;

import java.io.Serializable;
import java.util.Date;

public class WholeSaleBaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iid;

	private String cfullname;

	private String cemail;

	private Integer iwebsiteid;

	private String ctelephone;

	private String ccountrysn;

	private String cshipurl;

	private String cskype;

	private String ccomment;

	private String cshippingaddress;

	private Double fpurchaseamount;

	private Integer istatus;

	private Date dcreatedate;

	private Integer ilanguageid;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getCfullname() {
		return cfullname;
	}

	public void setCfullname(String cfullname) {
		this.cfullname = cfullname;
	}

	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public String getCtelephone() {
		return ctelephone;
	}

	public void setCtelephone(String ctelephone) {
		this.ctelephone = ctelephone;
	}

	public String getCcountrysn() {
		return ccountrysn;
	}

	public void setCcountrysn(String ccountrysn) {
		this.ccountrysn = ccountrysn;
	}

	public String getCshipurl() {
		return cshipurl;
	}

	public void setCshipurl(String cshipurl) {
		this.cshipurl = cshipurl;
	}

	public String getCskype() {
		return cskype;
	}

	public void setCskype(String cskype) {
		this.cskype = cskype;
	}

	public String getCcomment() {
		return ccomment;
	}

	public void setCcomment(String ccomment) {
		this.ccomment = ccomment;
	}

	public String getCshippingaddress() {
		return cshippingaddress;
	}

	public void setCshippingaddress(String cshippingaddress) {
		this.cshippingaddress = cshippingaddress;
	}

	public Double getFpurchaseamount() {
		return fpurchaseamount;
	}

	public void setFpurchaseamount(Double fpurchaseamount) {
		this.fpurchaseamount = fpurchaseamount;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}
}
