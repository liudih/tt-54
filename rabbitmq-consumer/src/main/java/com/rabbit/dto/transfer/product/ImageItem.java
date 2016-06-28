package com.rabbit.dto.transfer.product;

public class ImageItem {

	private String imageUrl;

	private String label;

	/**
	 * 排序
	 */
	private Integer order;

	private Boolean thumbnail;

	private Boolean smallImage;

	private Boolean baseImage;
	
	private Boolean showOnDetails;//是否显示在产品详情页面
	
	private String coriginalurl; //原始url
    
    private Boolean bfetch; //是否已经抓取到erp图片
    
    
	public String getCoriginalurl() {
		return coriginalurl;
	}

	public void setCoriginalurl(String coriginalurl) {
		this.coriginalurl = coriginalurl;
	}

	public Boolean getBfetch() {
		return bfetch;
	}

	public void setBfetch(Boolean bfetch) {
		this.bfetch = bfetch;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Boolean getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Boolean thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Boolean getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(Boolean smallImage) {
		this.smallImage = smallImage;
	}

	public Boolean getBaseImage() {
		return baseImage;
	}

	public void setBaseImage(Boolean baseImage) {
		this.baseImage = baseImage;
	}

	public Boolean getShowOnDetails() {
		return showOnDetails;
	}

	public void setShowOnDetails(Boolean showOnDetails) {
		this.showOnDetails = showOnDetails;
	}

}
