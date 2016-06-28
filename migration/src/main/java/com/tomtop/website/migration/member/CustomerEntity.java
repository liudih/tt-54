package com.tomtop.website.migration.member;

import java.util.Date;

public class CustomerEntity {

	int entity_id;
	int entity_type_id;
	int attribute_set_id;
	int website_id;
	String email;
	int group_id;
	String increment_id;
	int store_id;
	Date created_at;
	Date updated_at;
	boolean is_active;
	boolean disable_auto_group_change;

	public int getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(int entity_id) {
		this.entity_id = entity_id;
	}

	public int getEntity_type_id() {
		return entity_type_id;
	}

	public void setEntity_type_id(int entity_type_id) {
		this.entity_type_id = entity_type_id;
	}

	public int getAttribute_set_id() {
		return attribute_set_id;
	}

	public void setAttribute_set_id(int attribute_set_id) {
		this.attribute_set_id = attribute_set_id;
	}

	public int getWebsite_id() {
		return website_id;
	}

	public void setWebsite_id(int website_id) {
		this.website_id = website_id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getIncrement_id() {
		return increment_id;
	}

	public void setIncrement_id(String increment_id) {
		this.increment_id = increment_id;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public boolean isDisable_auto_group_change() {
		return disable_auto_group_change;
	}

	public void setDisable_auto_group_change(boolean disable_auto_group_change) {
		this.disable_auto_group_change = disable_auto_group_change;
	}

}
