package com.tomtop.product.models.vo;

public class SearchProductVo extends ProductBasePriceReviewCollectInfoVo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3044070625874299427L;
	/**
	 * 是否免邮
	 */
	private boolean isFreeShipping;
	
	public boolean isFreeShipping() {
		return isFreeShipping;
	}
	public void setFreeShipping(boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}
	
}
