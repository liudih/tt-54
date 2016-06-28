package com.tomtop.product.models.dto;


import java.util.Date;

/**
 * 产品多语言
 * @author xcf
 *
 */
public class ProductBaseTranslate {
	private Integer iid;

	private String clistingid;

	private Integer iwebsiteid;

	private Integer ilanguageid;

	private String csku;

	private String cparentsku;

	private Integer iqty;

	private Integer istatus;

	private boolean bmultiattribute;

	private Date dnewtodate;

	private boolean bspecial;

	private boolean bvisible;

	private double fprice;

	private double fweight;

	private Date dcreatedate;

	private String ctitle;

	private String cdescription;

	private String cshortdescription;

	private String ckeyword;

	private Integer icategory;

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

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIlanguageid() {
		return ilanguageid;
	}

	public void setIlanguageid(Integer ilanguageid) {
		this.ilanguageid = ilanguageid;
	}

	public String getCsku() {
		return csku;
	}

	public void setCsku(String csku) {
		this.csku = csku;
	}

	public Date getDnewtodate() {
		return dnewtodate;
	}

	public void setDnewtodate(Date dnewtodate) {
		this.dnewtodate = dnewtodate;
	}

	public boolean isBspecial() {
		return bspecial;
	}

	public void setBspecial(boolean bspecial) {
		this.bspecial = bspecial;
	}

	public double getFprice() {
		return fprice;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public double getFweight() {
		return fweight;
	}

	public void setFweight(double fweight) {
		this.fweight = fweight;
	}

	public Date getDcreatedate() {
		return dcreatedate;
	}

	public void setDcreatedate(Date dcreatedate) {
		this.dcreatedate = dcreatedate;
	}

	public String getCtitle() {
		return ctitle;
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}

	public String getCshortdescription() {
		return cshortdescription;
	}

	public void setCshortdescription(String cshortdescription) {
		this.cshortdescription = cshortdescription;
	}

	public String getCkeyword() {
		return ckeyword;
	}

	public void setCkeyword(String ckeyword) {
		this.ckeyword = ckeyword;
	}

	public String getCparentsku() {
		return cparentsku;
	}

	public void setCparentsku(String cparentsku) {
		this.cparentsku = cparentsku;
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public boolean isBmultiattribute() {
		return bmultiattribute;
	}

	public void setBmultiattribute(boolean bmultiattribute) {
		this.bmultiattribute = bmultiattribute;
	}

	public boolean isBvisible() {
		return bvisible;
	}

	public void setBvisible(boolean bvisible) {
		this.bvisible = bvisible;
	}

	public Integer getIcategory() {
		return icategory;
	}

	public void setIcategory(Integer icategory) {
		this.icategory = icategory;
	}

}
