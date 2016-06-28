package com.tomtop.product.price;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tomtop.product.mappers.product.ProductBaseMapper;
import com.tomtop.product.models.dto.ProductBase;
import com.tomtop.product.models.dto.price.Currency;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceBuilder;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.base.ICurrencyService;
import com.tomtop.product.services.price.IProductSpec;

@Component
public class BasePriceCalculator {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProductBaseMapper productMapper;

	@Autowired
	ICurrencyService currencyService;

	public List<Price> getPrice(final List<IProductSpec> items,
			final PriceCalculationContext ctx) {
		if (items != null && items.size() > 0) {
			List<String> listingIDs = Lists.newArrayList(Sets.newHashSet(Lists
					.transform(items, i -> i.getListingID())));
			logger.trace("Getting Basic Prices for Listings: {}", listingIDs);
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
										logger.warn(
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
