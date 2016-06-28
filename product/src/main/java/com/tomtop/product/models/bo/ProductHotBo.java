package com.tomtop.product.models.bo;


public class ProductHotBo extends BaseBo  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4250691636937312959L;
	private String listingId;
	private String sku;
	/**
	 * 商品标题
	 */
	private String title;
	/**
	 * 现价格
	 */
	private String nowprice;
	/**
	 * 原价
	 */
	private String origprice;
	/**
	 * 图片地址
	 */
	private String imgUrl;
	/**
	 * 路由地址
	 */
	private String url;

	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNowprice() {
		return nowprice;
	}
	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}
	public String getOrigprice() {
		return origprice;
	}
	public void setOrigprice(String origprice) {
		this.origprice = origprice;
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
	
}
