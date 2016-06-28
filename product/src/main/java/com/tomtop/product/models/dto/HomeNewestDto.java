package com.tomtop.product.models.dto;


import com.tomtop.product.models.bo.BaseBo;

public class HomeNewestDto extends BaseBo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2421426574747572597L;
	private String type;
	private String content;
	private String title;
	private String listingId;
	private String sku;
	private String userBy;
	private String country;
	
	public String getType() {
		return checkNull(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return checkNull(content);
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return checkNull(title);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getListingId() {
		return checkNull(listingId);
	}
	public void setListingId(String listingId) {
		this.listingId = listingId;
	}
	public String getSku() {
		return checkNull(sku);
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getUserBy() {
		return checkNull(userBy);
	}
	public void setUserBy(String userBy) {
		this.userBy = userBy;
	}
	public String getCountry() {
		return checkNull(country);
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
