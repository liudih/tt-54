package com.tomtop.website.migration.product;

import java.util.List;

public class MutilAttribute {

	String spu;
	String sku;
	String key;
	String value;
	Double price;
	String url;
	Integer status;
	Integer websiteId;
	List<com.website.dto.product.ImageItem> images;

	public String getSpu() {
		return spu;
	}

	public void setSpu(String spu) {
		this.spu = spu;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	public List<com.website.dto.product.ImageItem> getImages() {
		return images;
	}

	public void setImages(List<com.website.dto.product.ImageItem> images) {
		this.images = images;
	}


}
