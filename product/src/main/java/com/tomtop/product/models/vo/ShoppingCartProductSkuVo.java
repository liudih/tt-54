package com.tomtop.product.models.vo;

import java.io.Serializable;

/**
 * 购物车产品信息vo
 * 
 * @author liulj
 *
 */
public class ShoppingCartProductSkuVo extends ProductBasePriceInfoVo implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8496888694562432139L;

	private Integer num;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
