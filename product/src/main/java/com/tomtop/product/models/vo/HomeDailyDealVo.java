package com.tomtop.product.models.vo;

public class HomeDailyDealVo extends ProductBasePriceInfoVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8486774811625841096L;

	/**
	 * 折扣
	 */
	private Float discount;

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}
}