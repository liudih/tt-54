package com.tomtop.website.migration.category;

import java.util.List;

import com.website.dto.Attribute;

public class CategoryAttribute {
	String categoryPath;
	private List<Attribute> attributes;
/*	String key;
	List<String> values;*/

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}

	/*public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}*/

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

}
