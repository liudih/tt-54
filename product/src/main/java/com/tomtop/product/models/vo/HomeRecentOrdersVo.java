package com.tomtop.product.models.vo;

/**
 * 首页最近订单信息
 * 
 * @author liulj
 *
 */
public class HomeRecentOrdersVo extends ProductBasePriceInfoVo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2247038737543161052L;
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String countryName) {
		this.country = countryName;
	}
}
