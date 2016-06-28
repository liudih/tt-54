package services.price;

import java.util.List;

import javax.inject.Inject;

import services.ICurrencyService;
import valueobjects.price.Price;
import valueobjects.price.PriceBuilder;
import valueobjects.price.PriceCalculationContext;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;

import dao.product.IProductSalePriceEnquiryDao;
import dto.product.ProductSalePrice;

public class SaleDiscountProvider implements IDiscountProvider {

	@Inject
	IProductSalePriceEnquiryDao productSalePriceEnquityDao;

	@Inject
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
		List<ProductSalePrice> onSales = this.productSalePriceEnquityDao
				.getProductSalePrice(context.getPriceAt(), checkIDs);
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
