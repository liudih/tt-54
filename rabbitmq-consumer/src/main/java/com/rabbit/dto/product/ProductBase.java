package com.rabbit.dto.product;

import java.io.Serializable;
import java.util.Date;

public class ProductBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String clistingid;

	private Integer iwebsiteid;

	private Integer ilanguageid;

	private String csku;

	private Integer istatus;

	private Date dnewformdate;

	private Date dnewtodate;

	private Boolean bspecial;

	private String cvideoaddress;

	private Integer iqty;

	private Double fprice;

	private Double fcostprice;

	private Double fweight;

	private String ctitle;

	private String cdescription;

	private String cshortdescription;

	private String ckeyword;

	private String cmetatitle;

	private String cmetakeyword;

	private String cmetadescription;

	private String cpaymentexplain;

	private String creturnexplain;

	private String cwarrantyexplain;

	private String ctitle_default;

	private String cdescription_default;

	private String cshortdescription_default;

	private String ckeyword_default;

	private String cmetatitle_default;

	private String cmetakeyword_default;

	private String cmetadescription_default;

	private Boolean bmultiattribute;

	private String cparentsku;

	private Boolean bvisible;

	private Boolean bpulish;

	private String ccreateuser;

	private Date dcreatedate;

	private Double ffreight;

	private Boolean bmain;

	private Boolean bactivity;

	public String getClistingid() {
		return clistingid;
	}

	public void setClistingid(String clistingid) {
		this.clistingid = clistingid == null ? null : clistingid.trim();
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
		this.csku = csku == null ? null : csku.trim();
	}

	public Integer getIstatus() {
		return istatus;
	}

	public void setIstatus(Integer istatus) {
		this.istatus = istatus;
	}

	public Date getDnewformdate() {
		return dnewformdate;
	}

	public void setDnewformdate(Date dnewformdate) {
		this.dnewformdate = dnewformdate;
	}

	public Date getDnewtodate() {
		return dnewtodate;
	}

	public void setDnewtodate(Date dnewtodate) {
		this.dnewtodate = dnewtodate;
	}

	public Boolean getBspecial() {
		return bspecial;
	}

	public void setBspecial(Boolean ispecial) {
		this.bspecial = ispecial;
	}

	public String getCvideoaddress() {
		return cvideoaddress;
	}

	public void setCvideoaddress(String cvideoaddress) {
		this.cvideoaddress = cvideoaddress == null ? null : cvideoaddress
				.trim();
	}

	public Integer getIqty() {
		return iqty;
	}

	public void setIqty(Integer iqty) {
		this.iqty = iqty;
	}

	public Double getFprice() {
		return fprice;
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}

	public Double getFcostprice() {
		return fcostprice;
	}

	public void setFcostprice(Double fcostprice) {
		this.fcostprice = fcostprice;
	}

	public String getCtitle() {
		return ctitle == null ? ctitle_default : ctitle.trim();
	}

	public void setCtitle(String ctitle) {
		this.ctitle = ctitle == null ? null : ctitle.trim();
	}

	public String getCdescription() {
		return cdescription == null ? cdescription_default : cdescription
				.trim();
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription == null ? null : cdescription.trim();
	}

	public String getCshortdescription() {
		return cshortdescription == null ? cshortdescription_default
				: cshortdescription.trim();
	}

	public void setCshortdescription(String cshortdescription) {
		this.cshortdescription = cshortdescription == null ? null
				: cshortdescription.trim();
	}

	public String getCkeyword() {
		return ckeyword == null ? ckeyword_default : ckeyword.trim();
	}

	public void setCkeyword(String ckeyword) {
		this.ckeyword = ckeyword == null ? null : ckeyword.trim();
	}

	public String getCmetatitle() {
		return cmetatitle == null ? cmetatitle_default : cmetatitle.trim();
	}

	public void setCmetatitle(String cmetatitle) {
		this.cmetatitle = cmetatitle == null ? "" : cmetatitle.trim();
	}

	public String getCmetakeyword() {
		return cmetakeyword == null ? cmetakeyword_default : cmetakeyword
				.trim();
	}

	public void setCmetakeyword(String cmetakeyword) {
		this.cmetakeyword = cmetakeyword == null ? "" : cmetakeyword.trim();
	}

	public String getCmetadescription() {
		return cmetadescription == null ? cmetadescription_default
				: cmetadescription.trim();
	}

	public void setCmetadescription(String cmetadescription) {
		this.cmetadescription = cmetadescription == null ? null
				: cmetadescription.trim();
	}

	public String getCpaymentexplain() {
		return cpaymentexplain;
	}

	public void setCpaymentexplain(String cpaymentexplain) {
		this.cpaymentexplain = cpaymentexplain;
	}

	public String getCreturnexplain() {
		return creturnexplain;
	}

	public void setCreturnexplain(String creturnexplain) {
		this.creturnexplain = creturnexplain;
	}

	public String getCwarrantyexplain() {
		return cwarrantyexplain;
	}

	public void setCwarrantyexplain(String cwarrantyexplain) {
		this.cwarrantyexplain = cwarrantyexplain;
	}

	public String getCtitle_default() {
		return ctitle_default;
	}

	public void setCtitle_default(String ctitle_default) {
		this.ctitle_default = ctitle_default;
	}

	public String getCdescription_default() {
		return cdescription_default;
	}

	public void setCdescription_default(String cdescription_default) {
		this.cdescription_default = cdescription_default;
	}

	public String getCshortdescription_default() {
		return cshortdescription_default;
	}

	public void setCshortdescription_default(String cshortdescription_default) {
		this.cshortdescription_default = cshortdescription_default;
	}

	public String getCkeyword_default() {
		return ckeyword_default;
	}

	public void setCkeyword_default(String ckeyword_default) {
		this.ckeyword_default = ckeyword_default;
	}

	public String getCmetatitle_default() {
		return cmetatitle_default == null ? "" : cmetatitle_default.trim();
	}

	public void setCmetatitle_default(String cmetatitle_default) {
		this.cmetatitle_default = cmetatitle_default == null ? ""
				: cmetatitle_default;
	}

	public String getCmetakeyword_default() {
		return cmetakeyword_default;
	}

	public void setCmetakeyword_default(String cmetakeyword_default) {
		this.cmetakeyword_default = cmetakeyword_default;
	}

	public String getCmetadescription_default() {
		return cmetadescription_default;
	}

	public void setCmetadescription_default(String cmetadescription_default) {
		this.cmetadescription_default = cmetadescription_default;
	}

	public Boolean getBmultiattribute() {
		return bmultiattribute;
	}

	public void setBmultiattribute(Boolean bmultiattribute) {
		this.bmultiattribute = bmultiattribute;
	}

	public String getCparentsku() {
		return cparentsku;
	}

	public void setCparentsku(String cparentsku) {
		this.cparentsku = cparentsku == null ? null : cparentsku.trim();
	}

	public Boolean getBvisible() {
		return bvisible;
	}

	public void setBvisible(Boolean bvisible) {
		this.bvisible = bvisible;
	}

	public Boolean getBpulish() {
		return bpulish;
	}

	public void setBpulish(Boolean bpulish) {
		this.bpulish = bpulish;
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

	public Double getFweight() {
		return fweight;
	}

	public void setFweight(Double fweight) {
		this.fweight = fweight;
	}

	public Double getFfreight() {
		return ffreight;
	}

	public void setFfreight(Double ffreight) {
		this.ffreight = ffreight;
	}

	public Boolean getBmain() {
		return bmain;
	}

	public void setBmain(Boolean bmain) {
		this.bmain = bmain;
	}

	public Boolean getBactivity() {
		return bactivity;
	}

	public void setBactivity(Boolean bactivity) {
		this.bactivity = bactivity;
	}

}