package com.tomtop.product.models.bo;

import java.io.Serializable;

/**
 * 产品基本信息和价格vo类
 * 
 * @author liulj
 *
 */
public class ProductBaseInfoBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2009682257453165531L;

	private String listingId;

	private String sku;

	/**
	 * 标题
	 */
	String title;

	/**
	 * url
	 */
	String url;
	/**
	 * 图片地址
	 */
	String imageUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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
