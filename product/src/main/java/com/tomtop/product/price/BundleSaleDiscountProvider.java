package com.tomtop.product.price;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tomtop.product.dao.IProductSalePriceEnquiryDao;
import com.tomtop.product.models.dto.ProductSalePrice;
import com.tomtop.product.models.dto.price.BundlePrice;
import com.tomtop.product.models.dto.price.Price;
import com.tomtop.product.models.dto.price.PriceBuilder;
import com.tomtop.product.models.dto.price.PriceCalculationContext;
import com.tomtop.product.services.price.IDiscountProvider;
import com.tomtop.product.utils.DateFormatUtils;

@Component
public class BundleSaleDiscountProvider implements IDiscountProvider {

	@Autowired
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public List<Price> decorate(List<Price> originalPrices,
			PriceCalculationContext context) {

		List<String> checkIDs = PriceUtils
				.extractBundleProductSpecListingIDs(originalPrices);
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
		final Map<String, ProductSalePrice> salesIndex = Maps.uniqueIndex(
				onSales, s -> s.getClistingid());
		return Lists.transform(originalPrices, (Price p) -> {
			if (p instanceof BundlePrice) {
				return this.convertPrice((BundlePrice) p, salesIndex);
			}
			return p;
		});
	}

	private Price convertPrice(BundlePrice bprice,
			Map<String, ProductSalePrice> salesIndex) {

		List<Price> newpricelist = Lists.transform(
				bprice.getBreakdown(),
				p -> {
					if (salesIndex.containsKey(p.getSpec().getListingID())) {
						ProductSalePrice sp = salesIndex.get(p.getSpec()
								.getListingID());
						return PriceBuilder
								.change(p)
								.withNewPrice(sp.getFsaleprice(),
										sp.getDbegindate(), sp.getDenddate())
								.get();
					}
					return p;
				});
		double unitBasePrice = 0;
		double unitPrice = 0;
		for (Price child : newpricelist) {
			unitBasePrice += child.getUnitBasePrice();
			unitPrice += child.getUnitPrice();
		}
		BundlePrice result = new BundlePrice(bprice.getSpec(), newpricelist);
		result.setUnitBasePrice(unitBasePrice);
		result.setUnitPrice(unitPrice);
		result.setCurrency(bprice.getCurrency());
		result.setSymbol(bprice.getSymbol());
		result.setRate(bprice.getRate());
		if (unitBasePrice != unitPrice) {
			result.setDiscount((unitBasePrice - unitPrice) / unitBasePrice);
		}
		return result;
	}
}
