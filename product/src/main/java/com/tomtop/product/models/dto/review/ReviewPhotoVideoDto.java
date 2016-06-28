package com.tomtop.product.models.dto.review;

public class ReviewPhotoVideoDto {

	private Integer commentid;
	private String code;
	private String listingid;
	private String url;
	
	public Integer getCommentid() {
		return commentid;
	}
	public void setCommentid(Integer commentid) {
		this.commentid = commentid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getListingid() {
		return listingid;
	}
	public void setListingid(String listingid) {
		this.listingid = listingid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
