package com.tomtop.product.models.dto;

public class ProductsQueryDto extends BaseQueryDto {
	/**
	 * 分类编码
	 */
	private String categoryCode;
	/**
	 * 分类名称
	 */
	private String categoryName;
	/**
	 * 商品编码
	 */
	private String productsCode;
	/**
	 * 商品名称
	 */
	private String productsName;
	/**
	 * 商品描述
	 */
	private String productsDesc;
	/**
	 * 规格编码
	 */
	private String specCode;
	/**
	 * 款式名称
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
	 * 是否为特惠商品
	 */
	private Integer isDiscount;
	/**
	 * 特惠价
	 */
	private Integer discountPrice;
	/**
	 * 是否上架
	 */
	private Integer isOpenShelf;
	/**
	 * 是否启用状态(1:启用,0:停用)
	 */
	private Integer isEffective;
	/**
	 * 支持的售后服务
	 */
	private Integer afterSaleServices;
	/**
	 * 访问页面
	 */
	private Integer labelRegion;
	/**
	 * 是否限购
	 */
	private Integer islimit;
	/**
	 * @return the labelRegion
	 */
	public Integer getLabelRegion() {
		return labelRegion;
	}
	/**
	 * @param labelRegion the labelRegion to set
	 */
	public void setLabelRegion(Integer labelRegion) {
		this.labelRegion = labelRegion;
	}
	
	private static final long serialVersionUID = 4141556135181923632L;

	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getproductsCode() {
		return productsCode;
	}
	public void setproductsCode(String productsCode) {
		this.productsCode = productsCode;
	}
	public String getproductsName() {
		return productsName;
	}
	public void setproductsName(String productsName) {
		this.productsName = productsName;
	}
	public String getproductsDesc() {
		return productsDesc;
	}
	public void setproductsDesc(String productsDesc) {
		this.productsDesc = productsDesc;
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
	public Integer getIsDiscount() {
		return isDiscount;
	}
	public void setIsDiscount(Integer isDiscount) {
		this.isDiscount = isDiscount;
	}
	public Integer getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Integer discountPrice) {
		this.discountPrice = discountPrice;
	}
	public Integer getIsOpenShelf() {
		return isOpenShelf;
	}
	public void setIsOpenShelf(Integer isOpenShelf) {
		this.isOpenShelf = isOpenShelf;
	}
	public Integer getIsEffective(){
		return isEffective;
	}
	public void setIsEffective(Integer isEffective){
		this.isEffective = isEffective;
	}
	public Integer getAfterSaleServices() {
		return afterSaleServices;
	}
	public void setAfterSaleServices(Integer afterSaleServices) {
		this.afterSaleServices = afterSaleServices;
	}
}
