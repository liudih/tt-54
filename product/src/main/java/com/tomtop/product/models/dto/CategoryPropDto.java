package com.tomtop.product.models.dto;

import java.util.List;

public class CategoryPropDto extends BaseDto{
	
	private static final long serialVersionUID = -3389135819592649038L;
	/**
	 * 商品规格编号
	 */
	private String specCode;
	/**
	 * 商品编码
	 */
	private String ProductsCode;
	/**
	 * 商品分类编码
	 */
	private String categoryCode;
	/**
	 * 附属属性
	 */
	private List<ProductsCategoryPropDto> attachProps;
	/**
	 * 规格属性
	 */
	private List<ProductsCategoryPropDto> specProps;
	
	public String getProductsCode() {
		return ProductsCode;
	}
	public void setProductsCode(String ProductsCode) {
		this.ProductsCode = ProductsCode;
	}
	public String getSpecCode() {
		return specCode;
	}
	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public List<ProductsCategoryPropDto> getAttachProps() {
		return attachProps;
	}
	public void setAttachProps(List<ProductsCategoryPropDto> attachProps) {
		this.attachProps = attachProps;
	}
	public List<ProductsCategoryPropDto> getSpecProps() {
		return specProps;
	}
	public void setSpecProps(List<ProductsCategoryPropDto> specProps) {
		this.specProps = specProps;
	}
}
