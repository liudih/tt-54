package com.tomtop.product.price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.price.IPriceProvider;
import com.tomtop.product.services.price.IProductSpec;
import com.tomtop.product.services.price.impl.SingleProductSpec;

/**
 * 基础价格提供，适合找出单个商品的价格
 * 
 * @author kmtong
 *
 */
@Component
public class BasicPriceProvider implements IPriceProvider {

	@Autowired
	BasePriceCalculator basePriceCalculator;

	@Override
	public List<Price> getPrice(List<IProductSpec> items,
			PriceCalculationContext ctx) {

		return basePriceCalculator.getPrice(items, ctx);

	}

	@Override
	public boolean match(IProductSpec item) {
		return (item instanceof SingleProductSpec);
	}

}
