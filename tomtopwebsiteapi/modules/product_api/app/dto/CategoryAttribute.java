package dto;

import java.io.Serializable;

public class CategoryAttribute implements Serializable {
	private static final long serialVersionUID = 1L;
	private String attributeKey;
	private String attributeValue;
	private Integer attributeKeyId;
	private Integer attributeValueId;
	private Integer categoryId;
	private Integer ikeyid;

	public String getAttributeKey() {
		return attributeKey;
	}

	public void setAttributeKey(String attributeKey) {
		this.attributeKey = attributeKey;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Integer getAttributeKeyId() {
		return attributeKeyId;
	}

	public void setAttributeKeyId(Integer attributeKeyId) {
		this.attributeKeyId = attributeKeyId;
	}

	public Integer getAttributeValueId() {
		return attributeValueId;
	}

	public void setAttributeValueId(Integer attributeValueId) {
		this.attributeValueId = attributeValueId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getIkeyid() {
		return ikeyid;
	}

	public void setIkeyid(Integer ikeyid) {
		this.ikeyid = ikeyid;
	}
}
