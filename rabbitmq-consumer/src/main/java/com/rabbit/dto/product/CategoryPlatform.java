package com.rabbit.dto.product;

import java.io.Serializable;

public class CategoryPlatform implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer iid;
	Integer iwebsiteid;
	Integer icategoryid;
	Integer iparentid;
	String cpath;
	Integer ilevel;
	Integer iposition;
	Integer ichildrencount;
	String cmetadescription;
	String cmetatitle;
	String cmetakeyword;
	String ccontent;
	boolean bshow;
    boolean bhasbgimage;
    
	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getIwebsiteid() {
		return iwebsiteid;
	}

	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}

	public Integer getIcategoryid() {
		return icategoryid;
	}

	public void setIcategoryid(Integer icategoryid) {
		this.icategoryid = icategoryid;
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

	public Integer getIlevel() {
		return ilevel;
	}

	public void setIlevel(Integer ilevel) {
		this.ilevel = ilevel;
	}

	public Integer getIposition() {
		return iposition;
	}

	public void setIposition(Integer iposition) {
		this.iposition = iposition;
	}

	public Integer getIchildrencount() {
		return ichildrencount;
	}

	public void setIchildrencount(Integer ichildrencount) {
		this.ichildrencount = ichildrencount;
	}

	public String getCmetadescription() {
		return cmetadescription;
	}

	public void setCmetadescription(String cmetadescription) {
		this.cmetadescription = cmetadescription;
	}

	public String getCmetatitle() {
		return cmetatitle;
	}

	public void setCmetatitle(String cmetatitle) {
		this.cmetatitle = cmetatitle;
	}

	public String getCmetakeyword() {
		return cmetakeyword;
	}

	public void setCmetakeyword(String cmetakeyword) {
		this.cmetakeyword = cmetakeyword;
	}

	public boolean isBshow() {
		return bshow;
	}

	public void setBshow(boolean bshow) {
		this.bshow = bshow;
	}

	public String getCcontent() {
		return ccontent;
	}

	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}
	
	public boolean isBhasbgimage() {
		return bhasbgimage;
	}

	public void setBhasbgimage(boolean bhasbgimage) {
		this.bhasbgimage = bhasbgimage;
	}
}
