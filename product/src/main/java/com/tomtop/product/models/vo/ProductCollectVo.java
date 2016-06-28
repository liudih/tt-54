package com.tomtop.product.models.vo;

import java.io.Serializable;

public class ProductCollectVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String listingId;
	private String sku;
	private String email;
	private Integer lang;
	private Integer client;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getLang() {
		return lang;
	}
	public void setLang(Integer lang) {
		this.lang = lang;
	}
	public Integer getClient() {
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
}
