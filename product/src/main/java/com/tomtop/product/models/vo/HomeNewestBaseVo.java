package com.tomtop.product.models.vo;

import java.io.Serializable;

public class HomeNewestBaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2303254985376366451L;

	/**
	 * 标题
	 */
	private String skuTitle;

	/**
	 * url
	 */
	private String skuUrl;
	/**
	 * 图片地址
	 */
	private String skuImageUrl;
	public String getSkuTitle() {
		return skuTitle;
	}
	public void setSkuTitle(String skuTitle) {
		this.skuTitle = skuTitle;
	}
	public String getSkuUrl() {
		return skuUrl;
	}
	public void setSkuUrl(String skuUrl) {
		this.skuUrl = skuUrl;
	}
	public String getSkuImageUrl() {
		return skuImageUrl;
	}
	public void setSkuImageUrl(String skuImageUrl) {
		this.skuImageUrl = skuImageUrl;
	}
}
