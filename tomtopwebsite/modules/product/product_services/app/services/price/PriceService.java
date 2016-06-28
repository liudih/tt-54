package services.price;

import interceptors.CacheResult;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import services.base.CurrencyService;
import valueobjects.price.Price;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.ProductSpecBuilder;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PriceService implements IPriceService {

	@Inject
	Set<IPriceProvider> priceProviders;

	@Inject
	Set<IDiscountProvider> discountProviders;

	@Inject
	CurrencyService currency;

	@Inject
	PriceCalculationContextFactory ctxFactory;

	@Override
	public Price getPrice(String listingID) {
		return getPrice(ProductSpecBuilder.build(listingID).get(),
				ctxFactory.create());
	}

	@Override
	public Price getPrice(String listingID, String ccy) {
		return getPrice(ProductSpecBuilder.build(listingID).get(),
				ctxFactory.create(ccy));
	}

	@Override
	public Price getPrice(IProductSpec item) {
		return getPrice(item, ctxFactory.create());
	}

	@Override
	public Price getPrice(IProductSpec item, String ccy) {
		return getPrice(item, ctxFactory.create(ccy));
	}

	public Price getPrice(IProductSpec item, PriceCalculationContext ctx) {
		List<Price> prices = getPrice(Lists.newArrayList(item), ctx);
		if (prices != null && prices.size() == 1) {
			return prices.get(0);
		}
		Logger.error("Price Calculation Error: Listing={}, Prices={}",
				item.getListingID(), prices);
		return null;
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items) {
		return getPrice(items, ctxFactory.create());
	}

	@Override
	public List<Price> getPrice(List<IProductSpec> items, Date priceAt) {
		PriceCalculationContext ctx = ctxFactory.create();
		if (priceAt != null) {
			ctx.setPriceAt(priceAt);
		}
		return getPrice(items, ctx);
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
		for (List<Price> p : values) {
			flatten.addAll(p);
		}
		return flatten;
	}

	@Override
	public Map<IPriceProvider, List<Price>> getPriceByProvider(
			final List<IProductSpec> items) {
		return getPriceByProvider(items, ctxFactory.create());
	}

	@CacheResult("price")
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
