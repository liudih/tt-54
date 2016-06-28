package com.tomtop.product.models.vo;


public class HomeNewestImageVo extends HomeNewestBaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3271470048800321875L;

	private String imgUrl;

	private String imgBy;

	private String country;

	private String title;

	private String listingId;

	private String sku;

	public String getListingId() {
		return listingId;
	}

	public void setListingId(String listingId) {
		this.listingId = listingId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl == null ? null : imgUrl.trim();
	}

	public String getImgBy() {
		return imgBy;
	}

	public void setImgBy(String imgBy) {
		this.imgBy = imgBy == null ? null : imgBy.trim();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country == null ? null : country.trim();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}