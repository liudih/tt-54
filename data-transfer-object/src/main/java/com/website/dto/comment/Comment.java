package com.website.dto.comment;

import java.util.Date;
import java.util.List;

public class Comment {
	private Integer id;
	private String listingId;
	private String sku;
	private String memberEmail;
	private String comment;
	private Integer price;
	private Integer quality;
	private Integer shipping;
	private Integer usefulness;
	private Double foverallrating;
	private Date createdDate;
	private Date auditDate;	
	private Integer state;
	private Integer storeId;
	private Integer websiteId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getMemberEmail() {
		return memberEmail;
	}

	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public Integer getUsefulness() {
		return usefulness;
	}

	public void setUsefulness(Integer usefulness) {
		this.usefulness = usefulness;
	}

	public Double getFoverallrating() {
		return foverallrating;
	}

	public void setFoverallrating(Double foverallrating) {
		this.foverallrating = foverallrating;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}
	
	
	

}
