package com.tomtop.product.models.vo;

import java.io.Serializable;

public class WholesaleProductVo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1182941763311217944L;
	private String listingId;
	private String sku;
	private String email;
	private Integer qty;
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
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Integer getClient() {
		return client;
	}
	public void setClient(Integer client) {
		this.client = client;
	}
}
