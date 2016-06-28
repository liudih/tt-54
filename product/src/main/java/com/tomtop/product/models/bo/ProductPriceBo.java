package com.tomtop.product.models.bo;

import java.io.Serializable;

public class ProductPriceBo extends BaseBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6738952387553219551L;

	/**
	 * 现价格
	 */
	private String nowprice;

	/**
	 * 原价
	 */
	private String origprice;
	/**
	 * 货币使用的符号
	 */
	private String symbol = "$";

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
