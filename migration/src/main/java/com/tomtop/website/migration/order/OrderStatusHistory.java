package com.tomtop.website.migration.order;

import java.util.Date;

public class OrderStatusHistory {
	Integer entity_id;
	Integer parent_id;
	Integer is_customer_notified;
	Boolean is_visible_on_front;
	String comment;
	String status;
	Date created_at;
	String entity_name;

	public Integer getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(Integer entity_id) {
		this.entity_id = entity_id;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getIs_customer_notified() {
		return is_customer_notified;
	}

	public void setIs_customer_notified(Integer is_customer_notified) {
		this.is_customer_notified = is_customer_notified;
	}

	public Boolean getIs_visible_on_front() {
		return is_visible_on_front;
	}

	public void setIs_visible_on_front(Boolean is_visible_on_front) {
		this.is_visible_on_front = is_visible_on_front;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getEntity_name() {
		return entity_name;
	}

	public void setEntity_name(String entity_name) {
		this.entity_name = entity_name;
	}

}
