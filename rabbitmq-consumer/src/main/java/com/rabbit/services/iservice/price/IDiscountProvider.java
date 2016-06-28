package com.rabbit.services.iservice.price;

import java.util.List;

import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.price.PriceCalculationContext;

/**
 * 打折是相互排斥的，打折有优先顺序。根据自定义的优先值，越小越优先。
 * 
 * @author kmtong
 *
 */
public interface IDiscountProvider {

	/**
	 * 计算优惠的优先值，越小越优先
	 */
	int getPriority();

	List<Price> decorate(List<Price> originalPrices,
			PriceCalculationContext context);

}
