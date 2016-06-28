package com.tomtop.product.models.dto;
public class ProductsLabelDto extends BaseDto {
	private static final long serialVersionUID = 4940709063555772694L;
	// 商品标签id
	private Integer productsLabelId;
	// 标签id
	private Integer labelId;
	// 商品编码
	private String productsCode;
	//商品标签状态	
	private Integer labelStatus;
	//标签页名称
	private String labelRegionName;
	//标签名称
	private String labelName;
	//使用頁面id
	private Integer labelRegion;
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
	 * @return labelRegionName
	 */
	public String getLabelRegionName() {
		return labelRegionName;
	}
	/**
	 * @param labelRegionName 要设置的 labelRegionName
	 */
	public void setLabelRegionName(String labelRegionName) {
		this.labelRegionName = labelRegionName;
	}
	/**
	 * @return labelName
	 */
	public String getLabelName() {
		return labelName;
	}
	/**
	 * @param labelName 要设置的 labelName
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
	/**
	 * @return the productsLabelId
	 */
	public Integer getproductsLabelId() {
		return productsLabelId;
	}
	/**
	 * @return labelStatus
	 */
	public Integer getLabelStatus() {
		return labelStatus;
	}
	/**
	 * @param labelStatus 要设置的 labelStatus
	 */
	public void setLabelStatus(Integer labelStatus) {
		this.labelStatus = labelStatus;
	}
	/**
	 * @param productsLabelId the productsLabelId to set
	 */
	public void setproductsLabelId(Integer productsLabelId) {
		this.productsLabelId = productsLabelId;
	}
	/**
	 * @return labelId
	 */
	public Integer getLabelId() {
		return labelId;
	}
	/**
	 * @param labelId 要设置的 labelId
	 */
	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}
	/**
	 * @return the productsCode
	 */
	public String getproductsCode() {
		return productsCode;
	}
	/**
	 * @param productsCode the productsCode to set
	 */
	public void setproductsCode(String productsCode) {
		this.productsCode = productsCode;
	}

}
