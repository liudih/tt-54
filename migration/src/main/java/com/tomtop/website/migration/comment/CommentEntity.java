package com.tomtop.website.migration.comment;

import java.sql.Date;

public class CommentEntity {

	// review
	Integer review_id;
	Integer entity_id;
	Integer entity_pk_value; // product_id
	Integer status_id;
	Date created_at;
	String sku;

	// reviw_detail
	Integer detail_id;
	Integer store_id;
	String title;
	String detail;
	String nickname;
	Integer customer_id;

	// review_status
	String status_code;

	// review_entiry
	String entity_code;
	String email;
	Integer value;

	public Integer getReview_id() {
		return review_id;
	}

	public void setReview_id(Integer review_id) {
		this.review_id = review_id;
	}

	public Integer getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(Integer entity_id) {
		this.entity_id = entity_id;
	}

	public Integer getEntity_pk_value() {
		return entity_pk_value;
	}

	public void setEntity_pk_value(Integer entity_pk_value) {
		this.entity_pk_value = entity_pk_value;
	}

	public Integer getStatus_id() {
		return status_id;
	}

	public void setStatus_id(Integer status_id) {
		this.status_id = status_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(Integer detail_id) {
		this.detail_id = detail_id;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public String getStatus_code() {
		return status_code;
	}

	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}

	public String getEntity_code() {
		return entity_code;
	}

	public void setEntity_code(String entity_code) {
		this.entity_code = entity_code;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
