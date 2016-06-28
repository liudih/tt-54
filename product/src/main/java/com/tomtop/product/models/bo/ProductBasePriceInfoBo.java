package com.tomtop.product.models.bo;

/**
 * 产品基本信息和价格vo类
 * 
 * @author liulj
 *
 */
public class ProductBasePriceInfoBo extends ProductBaseInfoBo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3100460310608888869L;
	/**
	 * 货币使用的符号
	 */
	private String symbol = "$";
	/**
	 * 现价格
	 */
	private String nowprice;

	/**
	 * 原价
	 */
	private String origprice;

	public String getNowprice() {
		return nowprice;
	}

	public void setNowprice(String nowprice) {
		this.nowprice = nowprice;
	}

	public String getOrigprice() {
		return origprice;
	}

	public void setOrigprice(String origprice) {
		this.origprice = origprice;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	
}
