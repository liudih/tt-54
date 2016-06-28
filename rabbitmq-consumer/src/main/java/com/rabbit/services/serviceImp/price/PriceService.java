package com.rabbit.services.serviceImp.price;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rabbit.dto.valueobjects.price.Price;
import com.rabbit.dto.valueobjects.price.PriceCalculationContext;
import com.rabbit.dto.valueobjects.product.spec.IProductSpec;
import com.rabbit.services.iservice.price.IDiscountProvider;
import com.rabbit.services.iservice.price.IPriceProvider;
import com.rabbit.services.iservice.price.IPriceService;
@Service
public class PriceService implements IPriceService {

	private static Logger log=Logger.getLogger(PriceService.class.getName());
	@Autowired
	PriceCalculationContextFactory ctxFactory;
	@Autowired
	Set<IPriceProvider> priceProviders;
	@Autowired
	Set<IDiscountProvider> discountProviders;
	@Override
	public Price getPrice(IProductSpec item, String ccy) {
		return getPrice(item, ctxFactory.create(ccy));
	}

	public Price getPrice(IProductSpec item, PriceCalculationContext ctx) {
		List<Price> prices = getPrice(Lists.newArrayList(item), ctx);
		if (prices != null && prices.size() == 1) {
			return prices.get(0);
		}
		log.error("Price Calculation Error: Listing={}, Prices={}"+
				item.getListingID()+"   :"+ prices);
		return null;
	}
	@Override
	public List<Price> getPrice(List<IProductSpec> items,
			final PriceCalculationContext ctx) {
		Collection<List<Price>> values = getPriceByProvider(items, ctx)
				.values();
		List<Price> flatten = Lists.newLinkedList();
		for (List<Price> p : values) {
			flatten.addAll(p);
		}
		return flatten;
	}
	/*@CacheResult("price")*/
	public Map<IPriceProvider, List<Price>> getPriceByProvider(
			final List<IProductSpec> items, final PriceCalculationContext ctx) {
		Map<IPriceProvider, List<Price>> priceMap = Maps.asMap(
				priceProviders,
				pp -> {
					List<IProductSpec> filtered = Lists.newArrayList(Iterables
							.filter(items, i -> pp.match(i)));
					List<Price> prices = pp.getPrice(filtered, ctx);
					return prices;
				});

		List<IDiscountProvider> dps = Lists.newArrayList(discountProviders);
		Collections.sort(dps, (Comparator<IDiscountProvider>) (a, b) -> (a
				.getPriority() - b.getPriority()));
		Map<IPriceProvider, List<Price>> discounted = Maps.transformValues(
				priceMap, (List<Price> lp) -> {
					List<Price> current = lp;
					for (IDiscountProvider dp : dps) {
						current = dp.decorate(current, ctx);
					}
					return current;
				});
		return discounted;
	}


}
