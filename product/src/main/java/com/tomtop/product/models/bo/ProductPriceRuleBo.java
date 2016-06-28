package com.tomtop.product.models.bo;

/**
 * 产品价格类
 * 
 * @author liulj
 *
 */
public class ProductPriceRuleBo {
	/**
	 * 成本价
	 * 
	 */
	private String originalPrice;
	/**
	 * 销售价
	 */
	private String price;
	/**
	 * 促销结束日期
	 */
	private String endDate = "";
	
	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String constPrice) {
		this.originalPrice = constPrice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getEndDate() {
		if(endDate == null){
			endDate = "";
		}
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
