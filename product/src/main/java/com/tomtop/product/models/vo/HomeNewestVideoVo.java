package com.tomtop.product.models.vo;


public class HomeNewestVideoVo extends HomeNewestBaseVo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1608773255338344880L;

	private String videoUrl;

	private String videoBy;

	private String country;

	private String title;

	private String listingId;

	private String sku;

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl == null ? null : videoUrl.trim();
	}

	public String getVideoBy() {
		return videoBy;
	}

	public void setVideoBy(String reviewBy) {
		this.videoBy = reviewBy == null ? null : reviewBy.trim();
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
}