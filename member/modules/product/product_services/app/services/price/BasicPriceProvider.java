package services.price;

import java.util.List;

import javax.inject.Inject;

import valueobjects.price.Price;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.SingleProductSpec;

/**
 * 基础价格提供，适合找出单个商品的价格
 * 
 * @author kmtong
 *
 */
public class BasicPriceProvider implements IPriceProvider {

	@Inject
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
