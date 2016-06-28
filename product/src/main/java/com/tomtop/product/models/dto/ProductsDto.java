package com.tomtop.product.models.dto;

import java.util.List;
import java.util.Map;

public class ProductsDto extends BaseDto {
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
	 * 商品关联图片
	 */
	private Map<String, Object> productsImgs;
	/**
	 * 分类编码
	 */
	private String categoryCode;
	/**
	 * 分类名称
	 */
	private String categoryName;
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
	 * 是否为特价商品
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
	 * 可用库存
	 */
	private Integer availableStock;
	/**
	 * wms真实库存
	 */
	private Integer wmsStock;
	/**
	 * 支持的售后服务
	 */
	private Integer afterSaleServices;
	/**
	 * 包装参数
	 */
	private String packagingParams;
	/**
	 * 包装详情
	 */
	private String packagingDetail;
	/**
	 * 规格明细
	 */
	private String specificDetail;
	/**
	 * 条形码、IEME号
	 */
	private String wmsCode;
	/**
	 * 库存编码审核结果
	 */
	private Integer wmsCodeAudit;
	/**
	 * 商品标签图片
	 */
	private String productsLableImg;
	/**
	 * 使用頁面
	 */
	private Integer labelRegion;
	/**
	 * 商品关联属性
	 */
	List<ProductsPropLinkDto> productsPropLinks;
	/**
	 * 规格属性
	 */
	private List<ProductsCategoryPropDto> specProps;
	/**
	 * 关联商品与属性映射
	 */
	List<ProductsPropLinkDto> propLinkBrothers;

	/**
	 * 商品概述
	 * 
	 * @author Chentao 2014-3-3
	 */
	private String summary;

	/**
	 * 维保时间单位天
	 */
	private Integer warranty;
	/**
	 * 商品重量单位克
	 */
	private String weight;
	/**
	 * 是否支持退换货
	 */
	private Integer isRefund;
	/**
	 * @return the isRefund
	 */
	public Integer getIsRefund() {
		return isRefund;
	}

	/**
	 * @param isRefund the isRefund to set
	 */
	public void setIsRefund(Integer isRefund) {
		this.isRefund = isRefund;
	}

	/**
	 * 商品所属一级分类
	 */
	private String topCategoryCode;
	
	/**
	 * 限购数量
	 */
	private Integer limitNum;
	/**
	 * 是否限购
	 */
	private Integer islimit;
	/**
	 * 包退天数
	 */
	private Integer refundday;
	/**
	 * 是否换货（默认为1,2代表是退货）
	 */
	private Integer isswap;
	
	/**
	 * 包换天数
	 */
	private Integer swapday;
	/**
	 * 是否维修（默认为1,2代表是退货）
	 */
	private Integer isrepair;
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
	 * @return the limitNum
	 */
	public Integer getLimitNum() {
		return limitNum;
	}

	/**
	 * @param limitNum the limitNum to set
	 */
	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

	/**
	 * @return the islimit
	 */
	public Integer getIslimit() {
		return islimit;
	}

	/**
	 * @param islimit the islimit to set
	 */
	public void setIslimit(Integer islimit) {
		this.islimit = islimit;
	}

	/**
	 * @return the topCategoryCode
	 */
	public String getTopCategoryCode() {
		return topCategoryCode;
	}

	/**
	 * @param topCategoryCode the topCategoryCode to set
	 */
	public void setTopCategoryCode(String topCategoryCode) {
		this.topCategoryCode = topCategoryCode;
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

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	private static final long serialVersionUID = 1150858717242852810L;

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

	/**
	 * @return the productsLableImg
	 */
	public String getproductsLableImg() {
		return productsLableImg;
	}

	/**
	 * @param productsLableImg the productsLableImg to set
	 */
	public void setproductsLableImg(String productsLableImg) {
		this.productsLableImg = productsLableImg;
	}

	/**
	 * @return the wmsStock
	 */
	public Integer getWmsStock() {
		return wmsStock;
	}

	/**
	 * @param wmsStock the wmsStock to set
	 */
	public void setWmsStock(Integer wmsStock) {
		this.wmsStock = wmsStock;
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

	public Map<String, Object> getproductsImgs() {
		return productsImgs;
	}

	public void setproductsImgs(Map<String, Object> productsImgs) {
		this.productsImgs = productsImgs;
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

	public Integer getIsEffective() {
		return isEffective;
	}

	public void setIsEffective(Integer isEffective) {
		this.isEffective = isEffective;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public Integer getAfterSaleServices() {
		return afterSaleServices;
	}

	public void setAfterSaleServices(Integer afterSaleServices) {
		this.afterSaleServices = afterSaleServices;
	}

	public String getPackagingParams() {
		return packagingParams;
	}

	public void setPackagingParams(String packagingParams) {
		this.packagingParams = packagingParams;
	}

	public String getPackagingDetail() {
		return packagingDetail;
	}

	public void setPackagingDetail(String packagingDetail) {
		this.packagingDetail = packagingDetail;
	}

	public String getSpecificDetail() {
		return specificDetail;
	}

	public void setSpecificDetail(String specificDetail) {
		this.specificDetail = specificDetail;
	}

	public String getWmsCode() {
		return wmsCode;
	}

	public void setWmsCode(String wmsCode) {
		this.wmsCode = wmsCode;
	}

	public Integer getWmsCodeAudit() {
		return wmsCodeAudit;
	}

	public void setWmsCodeAudit(Integer wmsCodeAudit) {
		this.wmsCodeAudit = wmsCodeAudit;
	}

	public List<ProductsPropLinkDto> getproductsPropLinks() {
		return productsPropLinks;
	}

	public void setproductsPropLinks(List<ProductsPropLinkDto> productsPropLinks) {
		this.productsPropLinks = productsPropLinks;
	}

	public List<ProductsCategoryPropDto> getSpecProps() {
		return specProps;
	}

	public void setSpecProps(List<ProductsCategoryPropDto> specProps) {
		this.specProps = specProps;
	}

	public List<ProductsPropLinkDto> getPropLinkBrothers() {
		return propLinkBrothers;
	}

	public void setPropLinkBrothers(List<ProductsPropLinkDto> propLinkBrothers) {
		this.propLinkBrothers = propLinkBrothers;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}

