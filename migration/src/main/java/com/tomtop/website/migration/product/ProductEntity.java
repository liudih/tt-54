package com.tomtop.website.migration.product;

import java.util.Date;

public class ProductEntity {

	String sku;
	Date updatedDate;
	Integer entity_id;

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(Integer entity_id) {
		this.entity_id = entity_id;
	}


	
	

}
