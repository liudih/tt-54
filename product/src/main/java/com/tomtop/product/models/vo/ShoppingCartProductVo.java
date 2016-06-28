package com.tomtop.product.models.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 购物车产品信息vo
 * 
 * @author liulj
 *
 */
public class ShoppingCartProductVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8496888694562432139L;

	/**
	 * 商品信息
	 */
	public List<ProductBasePriceInfoVo> skus;

	/**
	 * 总价格
	 */
	public String total;

	public List<ProductBasePriceInfoVo> getSkus() {
		return skus;
	}

	public void setSkus(List<ProductBasePriceInfoVo> skus) {
		this.skus = skus;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
