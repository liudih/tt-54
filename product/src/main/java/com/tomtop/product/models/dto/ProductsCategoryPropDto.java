package com.tomtop.product.models.dto;

import java.io.Serializable;

public class ProductsCategoryPropDto extends BaseDto implements Serializable {
	
	private static final long serialVersionUID = 1121367643157405817L;
	/**
	 * 商品编码
	 */
	private String productsCode;
	/**
	 * 商品款式编码
	 */
	private String specCode;
	/**
	 * 规格名称
	 */
	private String specName;
	/**
	 * 商品分类编码
	 */
	private String categoryCode;
	/**
	 * 属性类型编码
	 */
	private Integer typeCode;
	/**
	 * 属性名称
	 */
	private String typeName;
	 /**
     * 分组名称
     */
    private String groupName;
	/**
	 * 属性内容编码
	 */
	private Integer valueCode;
	/**
	 * 属性内容值
	 */
	private String propValue;
	/**
	 * 属性内容值描述
	 */
	private String propValueDesc;
	 /**
     * 属性描述
     */
    private String typeDesc;
	/**
	 * 属性值图片路径
	 */
	private String valueImgPath;
	/**
	 * 是否停用
	 */
	private Integer isDisable;

	public String getproductsCode() {
		return productsCode;
	}
	public void setproductsCode(String productsCode) {
		this.productsCode = productsCode;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public Integer getIsDisable() {
		return isDisable;
	}
	public void setIsDisable(Integer isDisable) {
		this.isDisable = isDisable;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getValueCode() {
		return valueCode;
	}
	public void setValueCode(Integer valueCode) {
		this.valueCode = valueCode;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	public String getPropValueDesc() {
		return propValueDesc;
	}
	public void setPropValueDesc(String propValueDesc) {
		this.propValueDesc = propValueDesc;
	}
	public String getSpecCode() {
		return specCode;
	}
	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}
	public String getValueImgPath() {
		return valueImgPath;
	}
	public void setValueImgPath(String valueImgPath) {
		this.valueImgPath = valueImgPath;
	}
}