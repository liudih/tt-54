package com.tomtop.product.models.dto;

public class ProductsSpecQueryDto extends BaseQueryDto {
	/**
	 * 商品分类编码
	 */
	private String categoryCode;
	/**
	 * 规格号码
	 */
	private String specCode;
	/**
	 * 商品编码
	 */
	private String productsCode;
	/**
	 * 规格名称
	 */
	private String specName;
	/**
	 * 原价
	 */
	private Integer originalPrice;
	/**
	 * 销售价
	 */
	private Integer salePrice;
	/**
	 * 是否停用
	 */
	private Integer isDisable;
	
	/**
	 * 创建时间start
	 */
	private String modifytimeStart;
	/**
	 * 创建时间 begin
	 */
	private String modifytimeBegin;
	
	
	public String getproductsCode() {
		return productsCode;
	}
	public void setproductsCode(String productsCode) {
		this.productsCode = productsCode;
	}
	public String getModifytimeStart() {
		return modifytimeStart;
	}
	public void setModifytimeStart(String modifytimeStart) {
		this.modifytimeStart = modifytimeStart;
	}
	public String getModifytimeBegin() {
		return modifytimeBegin;
	}
	public void setModifytimeBegin(String modifytimeBegin) {
		this.modifytimeBegin = modifytimeBegin;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getSpecCode() {
		return specCode;
	}
	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public Integer getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}
	public Integer getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Integer salePrice) {
		this.salePrice = salePrice;
	}
	public Integer getIsDisable() {
		return isDisable;
	}
	public void setIsDisable(Integer isDisable) {
		this.isDisable = isDisable;
	}


	private static final long serialVersionUID = 3399049570339474158L;

}
