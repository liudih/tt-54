package com.tomtop.product.services.price.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.tomtop.product.dao.IProductSalePriceEnquiryDao;
import com.tomtop.product.models.dto.ProductSalePrice;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceBuilder;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.price.PriceUtils;
import com.tomtop.product.services.base.ICurrencyService;
import com.tomtop.product.services.price.IDiscountProvider;
import com.tomtop.product.utils.DateFormatUtils;

@Component
public class SaleDiscountProvider implements IDiscountProvider {

	@Autowired
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;

	@Autowired
	ICurrencyService exchg;

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<Price> decorate(List<Price> originalPrices,
			PriceCalculationContext context) {
		List<String> checkIDs = PriceUtils
				.extractSingleProductSpecListingIDs(originalPrices);
		if (checkIDs == null || checkIDs.size() == 0) {
			return originalPrices;
		}
		Date utcTime = null;
		try {
			utcTime = DateFormatUtils.getCurrentUtcTimeDate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<ProductSalePrice> onSales = this.productSalePriceEnquityDao
				.getProductSalePrice(utcTime, checkIDs);
		final ListMultimap<String, ProductSalePrice> salesIndex = Multimaps
				.index(onSales, s -> s.getClistingid());
		return Lists.transform(
				originalPrices,
				p -> {
					if (salesIndex.containsKey(p.getSpec().getListingID())) {
						List<ProductSalePrice> sps = salesIndex.get(p.getSpec()
								.getListingID());
						if (!sps.isEmpty()) {
							// if more than one sale price, get the lowest
							ProductSalePrice sp = Ordering
									.natural()
									.onResultOf(
											(ProductSalePrice pp) -> pp
													.getFsaleprice()).min(sps);
							// discount price even higher than original price?
							// no way, return original price
							double salePriceInTargetCcy = exchg.exchange(
									sp.getFsaleprice(), context.getCurrency());
							if (salePriceInTargetCcy >= p.getUnitPrice()) {
								return p;
							}
							return PriceBuilder
									.change(p)
									.withNewPrice(sp.getFsaleprice(),
											sp.getDbegindate(),
											sp.getDenddate()).get();
						}
					}
					return p;
				});
	}

}
