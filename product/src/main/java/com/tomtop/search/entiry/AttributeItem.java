package com.tomtop.search.entiry;

import java.io.Serializable;

/**
 * 多属性
 * @author ztiny
 * @Date 2015-12-21
 */
public class AttributeItem  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6961169128814851604L;
	//属性对应的ID
	private Integer keyId;
	//属性对应的键
	private String key;
	//属性值对应的ID
	private Integer valueId;
	//属性值对应的真实值
	private String value;
	//语言ID
	private Integer languangeId;
	//是否展示小图标
	private Boolean showImg;
	//属性是否可见
	private Boolean visible;

	public Integer getKeyId() {
		return keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
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

	public Integer getLanguangeId() {
		return languangeId;
	}

	public void setLanguangeId(Integer languangeId) {
		this.languangeId = languangeId;
	}

	public Boolean getShowImg() {
		return showImg;
	}

	public void setShowImg(Boolean showImg) {
		this.showImg = showImg;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	

}
