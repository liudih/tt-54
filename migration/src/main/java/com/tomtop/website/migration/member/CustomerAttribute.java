package com.tomtop.website.migration.member;

public class CustomerAttribute {

	int entity_id;
	int parent_id;
	String attribute_code;
	String value;

	public int getEntity_id() {
		return entity_id;
	}

	public void setEntity_id(int entity_id) {
		this.entity_id = entity_id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
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
