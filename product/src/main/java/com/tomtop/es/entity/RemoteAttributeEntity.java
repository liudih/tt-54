package com.tomtop.es.entity;

/**
 * 不同类目下面显示不同多属性的实体配置对象
 * @author ztiny
 *
 */
public class RemoteAttributeEntity {
	private int parentId; // 标签父Id
	private int keyid;// 标签id
	private int languageId;// 语言id
	private int clientId;// 客户端id
	private int isChange;// 是否是动态属性0 不是，1是
	private int level;// 等级
	private int categoryId;// 类目id
	private String attributeKey;// 属性key
	private String attributeValue;// 属性value
	private int sort;// 排序号

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getKeyid() {
		return keyid;
	}

	public void setKeyid(int keyid) {
		this.keyid = keyid;
	}

	public int getLanguageId() {
		return languageId;
	}

	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getIsChange() {
		return isChange;
	}

	public void setIsChange(int isChange) {
		this.isChange = isChange;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

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

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
