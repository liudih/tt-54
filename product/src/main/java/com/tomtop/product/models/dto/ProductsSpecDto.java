package com.tomtop.product.models.dto;

import java.util.List;

public class ProductsSpecDto extends BaseDto{
	/**
	 * 商品编码
	 */
	private String ProductsCode;
	/**
	 * 商品分类编码
	 */
	private String categoryCode;
	/**
	 * 规格号码
	 */
	private String specCode;
	/**
	 * 规格名称
	 */
	private String specName;
	/**
	 * 是否停用
	 */
	private Integer isDisable;
	/**
	 * 是否退货（默认为0,1代表是退货）
	 */
	private Integer isRefund;
	/**
	 * 包退天数
	 */
	private Integer refundday;
	/**
	 * 是否换货（默认为1,2代表是退货）
	 */
	private Integer isswap;
	
	/**
	 * @return the refundday
	 */
	public Integer getRefundday() {
		return refundday;
	}
	/**
	 * @param refundday the refundday to set
	 */
	public void setRefundday(Integer refundday) {
		this.refundday = refundday;
	}
	/**
	 * @return the isswap
	 */
	public Integer getIsswap() {
		return isswap;
	}
	/**
	 * @param isswap the isswap to set
	 */
	public void setIsswap(Integer isswap) {
		this.isswap = isswap;
	}
	/**
	 * @return the swapday
	 */
	public Integer getSwapday() {
		return swapday;
	}
	/**
	 * @param swapday the swapday to set
	 */
	public void setSwapday(Integer swapday) {
		this.swapday = swapday;
	}
	/**
	 * @return the isrepair
	 */
	public Integer getIsrepair() {
		return isrepair;
	}
	/**
	 * @param isrepair the isrepair to set
	 */
	public void setIsrepair(Integer isrepair) {
		this.isrepair = isrepair;
	}
	/**
	 * 包换天数
	 */
	private Integer swapday;
	/**
	 * 是否维修（默认为1,2代表是退货）
	 */
	private Integer isrepair;
	/**
	 * 保修期
	 */
	private Integer warranty;
	/**
	 * 商品重量
	 */
	private String weight;
	
	/**
	 * 商品尺寸
	 */
	private String size;
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
	private static final long serialVersionUID = 3954748968502406547L;
	
	public Integer getIsRefund() {
		return isRefund;
	}
	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}
	/**
	 * @return the warranty
	 */
	public Integer getWarranty() {
		return warranty;
	}
	/**
	 * @param warranty the warranty to set
	 */
	public void setWarranty(Integer warranty) {
		this.warranty = warranty;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
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
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
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
	public Integer getIsDisable() {
		return isDisable;
	}
	public void setIsDisable(Integer isDisable) {
		this.isDisable = isDisable;
	}
}
