package com.tomtop.product.models.bo;

public class ProductSeoBo extends BaseBo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//SEO title
	private String metaTitle;
	//Seo 描述
	private String metaDescription;
	//Seo关键字
	private String metaKeyword;
	
	public String getMetaTitle() {
		return metaTitle;
	}
	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
	public String getMetaKeyword() {
		return metaKeyword;
	}
	public void setMetaKeyword(String metaKeyword) {
		this.metaKeyword = metaKeyword;
	}
}
