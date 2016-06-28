package com.tomtop.product.services.price.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.price.PriceCalculationContextFactory;
import com.tomtop.product.price.ProductSpecBuilder;
import com.tomtop.product.services.base.ICurrencyService;
import com.tomtop.product.services.price.IDiscountProvider;
import com.tomtop.product.services.price.IPriceProvider;
import com.tomtop.product.services.price.IPriceService;
import com.tomtop.product.services.price.IProductSpec;

@Component
public class PriceService implements IPriceService {

	@Autowired
	Set<IPriceProvider> priceProviders;

	@Autowired
	Set<IDiscountProvider> discountProviders;

	@Autowired
	ICurrencyService currency;

	@Autowired
	PriceCalculationContextFactory ctxFactory;

	@Override
	public Price getPrice(String listingID, String ccy) {
		return getPrice(ProductSpecBuilder.build(listingID).get(),
				ctxFactory.create(ccy));
	}

	@Override
	public Price getPrice(IProductSpec item, String ccy) {
		return getPrice(item, ctxFactory.create(ccy));
	}

	@Override
	public Price getPrice(IProductSpec item, PriceCalculationContext ctx) {
		List<Price> prices = getPrice(Lists.newArrayList(item), ctx);
		if (prices != null && prices.size() == 1) {
			return prices.get(0);
		}
		LoggerFactory.getLogger(this.getClass()).error(
				"Price Calculation Error: Listing={}, Prices={}",
				item.getListingID(), prices);
		return null;
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items, Date priceAt,
			String currency) {
		PriceCalculationContext ctx = ctxFactory.create(currency);
		if (priceAt != null) {
			ctx.setPriceAt(priceAt);
		}
		return getPrice(items, ctx);
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items,
			final PriceCalculationContext ctx) {
		Collection<List<Price>> values = getPriceByProvider(items, ctx)
				.values();
		List<Price> flatten = Lists.newLinkedList();
		values.forEach(p -> flatten.addAll(p));
		/*
		 * for (List<Price> p : values) { flatten.addAll(p); }
		 */
		return flatten;
	}

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
