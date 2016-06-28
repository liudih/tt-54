package com.tomtop.product.models.dto;

import java.io.Serializable;
import java.util.List;

public class FindProductsDto implements Serializable{
	private static final long serialVersionUID = -8316775450800104807L;

	// 标签使用页面(:1.单品页2.购物车列表3.列表)
	private Integer labelRegion;

	private List<String> productsCodes;

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

	/**
	 * @return the productsCodes
	 */
	public List<String> getproductsCodes() {
		return productsCodes;
	}

	/**
	 * @param productsCodes
	 *            the productsCodes to set
	 */
	public void setproductsCodes(List<String> productsCodes) {
		this.productsCodes = productsCodes;
	}

}
