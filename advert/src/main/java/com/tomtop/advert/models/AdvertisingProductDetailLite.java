package com.tomtop.advert.models;

public class AdvertisingProductDetailLite {
	private String title;
	private String imgUrl;
	private String url;
//	private String listingId;
    private String cbusinessid;
    
    private Integer iwebsiteid;
    
 
	public String getCbusinessid() {
		return cbusinessid;
	}
	public void setCbusinessid(String cbusinessid) {
		this.cbusinessid = cbusinessid;
	}
	public Integer getIwebsiteid() {
		return iwebsiteid;
	}
	public void setIwebsiteid(Integer iwebsiteid) {
		this.iwebsiteid = iwebsiteid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
//	public String getListingId() {
//		return listingId;
//	}
//	public void setListingId(String listingId) {
//		this.listingId = listingId;
//	}
	
	
}
