package com.tomtop.website.migration.product;

public class ProductAttribute {
	String sku;
	String has_options;
	String attribute_id;
	String attribute_code;
	String value;
	Integer category_id;

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(Integer category_id) {
		this.category_id = category_id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getHas_options() {
		return has_options;
	}

	public void setHas_options(String has_options) {
		this.has_options = has_options;
	}

	public String getAttribute_id() {
		return attribute_id;
	}

	public void setAttribute_id(String attribute_id) {
		this.attribute_id = attribute_id;
	}

	public String getAttribute_code() {
		return attribute_code;
	}

	public void setAttribute_code(String attribute_code) {
		this.attribute_code = attribute_code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
