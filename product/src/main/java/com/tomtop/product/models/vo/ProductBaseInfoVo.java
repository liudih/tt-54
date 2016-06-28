package com.tomtop.product.models.vo;

import java.io.Serializable;

/**
 * 产品基本信息和价格vo类
 * 
 * @author liulj
 *
 */
public class ProductBaseInfoVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3342259978942541760L;

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

	private String listingId;

	private String sku;

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
