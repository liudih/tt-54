package com.tomtop.product.models.dto;

import java.io.Serializable;

public class CategoryWebsiteWithNameDto extends CategoryPlatformDto implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4374359958812874636L;

	String cname;

	Integer nameid;

	String cbgimglink;

	Integer ibgimageid;

	private String cdescription;

	private String metaTitle;
	private String metaKeyWords;
	private String metadescription;

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaKeyWords() {
		return metaKeyWords;
	}

	public void setMetaKeyWords(String metaKeyWord) {
		this.metaKeyWords = metaKeyWord;
	}

	public String getMetadescription() {
		return metadescription;
	}

	public void setMetadescription(String metadescription) {
		this.metadescription = metadescription;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getNameid() {
		return nameid;
	}

	public void setNameid(Integer nameid) {
		this.nameid = nameid;
	}

	public String getCbgimglink() {
		return cbgimglink;
	}

	public void setCbgimglink(String cbgimglink) {
		this.cbgimglink = cbgimglink;
	}

	public Integer getIbgimageid() {
		return ibgimageid;
	}

	public void setIbgimageid(Integer ibgimageid) {
		this.ibgimageid = ibgimageid;
	}

	public String getCdescription() {
		return cdescription;
	}

	public void setCdescription(String cdescription) {
		this.cdescription = cdescription;
	}
}
