package com.tomtop.product.models.vo;

public class HomeNewestReviewVo extends HomeNewestBaseVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4575293730076850300L;

	private String reviewContent;

	private String reviewBy;

	private String title;

	private String country;

	private String listingId;

	private String sku;

	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent == null ? null : reviewContent
				.trim();
	}

	public String getReviewBy() {
		return reviewBy;
	}

	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy == null ? null : reviewBy.trim();
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