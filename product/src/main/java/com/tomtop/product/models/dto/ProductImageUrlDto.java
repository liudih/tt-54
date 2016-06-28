package com.tomtop.product.models.dto;

public class ProductImageUrlDto {

	
	private Integer iid;
	private String clistingid;
	private String csku;
	private String cimageurl;
	private Integer iorder;
	private Boolean bthumbnail;
	private Boolean bsmallimage;
	private Boolean bbaseimage;
	
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
	public String getCsku() {
		return csku;
	}
	public void setCsku(String csku) {
		this.csku = csku;
	}
	public String getCimageurl() {
		return cimageurl;
	}
	public void setCimageurl(String cimageurl) {
		this.cimageurl = cimageurl;
	}
	public Integer getIorder() {
		return iorder;
	}
	public void setIorder(Integer iorder) {
		this.iorder = iorder;
	}
	public Boolean getBthumbnail() {
		return bthumbnail;
	}
	public void setBthumbnail(Boolean bthumbnail) {
		this.bthumbnail = bthumbnail;
	}
	public Boolean getBsmallimage() {
		return bsmallimage;
	}
	public void setBsmallimage(Boolean bsmallimage) {
		this.bsmallimage = bsmallimage;
	}
	public Boolean getBbaseimage() {
		return bbaseimage;
	}
	public void setBbaseimage(Boolean bbaseimage) {
		this.bbaseimage = bbaseimage;
	}
}
