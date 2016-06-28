package com.tomtop.product.models.dto;

public class LabelDto extends BaseDto {
	private static final long serialVersionUID = -7517018016607638044L;
	// 标签id
	private Integer labelId;
	// 标签名称
	private String labelName;
	// 标签图片地址
	private String labelImg;
	// 标签使用页面(:1.单品页2.购物车列表3.列表)
	private Integer labelRegion;
	// 标签使用页面名称
	private String labelRegionName;
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
	 * @return the labelId
	 */
	public Integer getLabelId() {
		return labelId;
	}

	/**
	 * @param labelId the labelId to set
	 */
	public void setLabelId(Integer labelId) {
		this.labelId = labelId;
	}

	/**
	 * @return the labelName
	 */
	public String getLabelName() {
		return labelName;
	}

	/**
	 * @param labelName
	 *            the labelName to set
	 */
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	/**
	 * @return the labelImg
	 */
	public String getLabelImg() {
		return labelImg;
	}

	/**
	 * @param labelImg
	 *            the labelImg to set
	 */
	public void setLabelImg(String labelImg) {
		this.labelImg = labelImg;
	}

	/**
	 * @return the labelRegion
	 */
	public Integer getLabelRegion() {
		return labelRegion;
	}

	/**
	 * @param labelRegion
	 *            the labelRegion to set
	 */
	public void setLabelRegion(Integer labelRegion) {
		this.labelRegion = labelRegion;
	}
}
