package com.tomtop.product.services;

import java.util.List;

import com.tomtop.product.models.bo.CurrencyBo;
import com.tomtop.product.models.bo.ProductPriceRuleBo;
import com.tomtop.search.entiry.PromotionPrice;

/**
 * 产品价格的算法规则
 * 
 * @author liulj
 *
 */
public interface IProductPriceRule {
	/**
	 * 获取价格，经过计算后的
	 * 
	 * @param constPrice
	 *            成本价
	 * @param price
	 *            销售价
	 * @param promsPrice
	 *            折扣价
	 * @param currency
	 *            货币
	 * @return
	 */
	ProductPriceRuleBo getPrice(Double constPrice, Double price,
			List<PromotionPrice> promsPrice, CurrencyBo currency);
	
	ProductPriceRuleBo getPriceEndDate(Double constPrice, Double price,
			List<PromotionPrice> promsPrice, CurrencyBo currency);
}
