package services.price;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductBaseMapper;
import play.Logger;
import services.base.CurrencyService;
import valueobjects.price.Price;
import valueobjects.price.PriceBuilder;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.spec.IProductSpec;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import dto.Currency;
import dto.product.ProductBase;

public class BasePriceCalculator {

	@Inject
	ProductBaseMapper productMapper;

	@Inject
	CurrencyService currencyService;

	public List<Price> getPrice(final List<IProductSpec> items,
			final PriceCalculationContext ctx) {
		if (items != null && items.size() > 0) {
			List<String> listingIDs = Lists.newArrayList(Sets.newHashSet(Lists
					.transform(items, i -> i.getListingID())));
			Logger.trace("Getting Basic Prices for Listings: {}", listingIDs);
			List<ProductBase> productsWithPriceOnly = productMapper
					.getBasePrice(listingIDs);
			final Map<String, ProductBase> productIndex = Maps.uniqueIndex(
					productsWithPriceOnly, p -> p.getClistingid());
			List<Price> relist = Lists
					.transform(
							items,
							i -> {
								ProductBase p = productIndex.get(i
										.getListingID());
								Currency ccy = currencyService
										.getCurrencyByCode(ctx.getCurrency());
								if (p != null) {
									double cost = p.getFprice();
									if (p.getFcostprice() != null) {
										cost = p.getFcostprice();
									} else {
										Logger.warn(
												"Listing {}: Unit Cost not set, setting cost to base price",
												i.getListingID());
									}
									return PriceBuilder
											.build(i, p.getFprice(), cost)
											.inCurrency(
													ctx.getCurrency(),
													ccy.getCsymbol(),
													currencyService.getRate(ctx
															.getCurrency()))
											.get();

								}
								return null;
							});
			relist.remove(null);
			return relist;
		}
		return Lists.newArrayList();
	}
}
