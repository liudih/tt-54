package com.tomtop.product.services.freight;

import java.util.HashMap;
import java.util.Map;

import com.tomtop.product.models.dto.shipping.ShippingMethodDetail;

public interface IFreightService {

	/**
	 * 
	 * @param map
	 *            listingID为key，qty为值
	 * @param isNeedFreeShippingLabel
	 * @return
	 */
	public abstract Double getTotalWeight(Map<String, Integer> map,
			Boolean isNeedFreeShippingLabel);

	/**
	 *
	 * @param shippingMethodId
	 * @param weight
	 *            (Units: g)
	 * @param shippingWeight
	 *            去掉免邮产品后的重量
	 * @param exchangeRate
	 * @return
	 */
	public Double getFinalFreight(ShippingMethodDetail shippingMethod,
			Double weight, Double shippingWeight, String currency,
			double grandTotal, boolean hasAllFreeShipping);
	
	public Double getFreight(ShippingMethodDetail shippingMethod,
			Double weight, String currency);
	
	/**
	 * 使用java内置的js脚本, 计算运费
	 * 
	 * @param weight
	 *            重量(g)
	 * @param exchangeRate
	 *            对美元汇率，如果空，则默认从接口中取得当前货币汇率
	 *
	 * @return Double型的运费 或者null, 无法运算
	 * */
	public abstract Double getFirstFreight(ShippingMethodDetail shippingMethod,
			Double weight);

	public abstract Double runJS(HashMap<String, String> subs, String ruleValue);

}