package com.tomtop.website.migration.product;

public class ProductGroupPrice {
	String sku;
	Double price;
	Double final_price;
	Double min_price;
	Double max_price;
	Double tier_price;
	Double group_price;
	Integer website_id;
	Integer customer_group_id;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getFinal_price() {
		return final_price;
	}

	public void setFinal_price(Double final_price) {
		this.final_price = final_price;
	}

	public Double getMin_price() {
		return min_price;
	}

	public void setMin_price(Double min_price) {
		this.min_price = min_price;
	}

	public Double getMax_price() {
		return max_price;
	}

	public void setMax_price(Double max_price) {
		this.max_price = max_price;
	}

	public Double getTier_price() {
		return tier_price;
	}

	public void setTier_price(Double tier_price) {
		this.tier_price = tier_price;
	}

	public Double getGroup_price() {
		return group_price;
	}

	public void setGroup_price(Double group_price) {
		this.group_price = group_price;
	}

	public Integer getWebsite_id() {
		return website_id;
	}

	public void setWebsite_id(Integer website_id) {
		this.website_id = website_id;
	}

	public Integer getCustomer_group_id() {
		return customer_group_id;
	}

	public void setCustomer_group_id(Integer customer_group_id) {
		this.customer_group_id = customer_group_id;
	}
}
