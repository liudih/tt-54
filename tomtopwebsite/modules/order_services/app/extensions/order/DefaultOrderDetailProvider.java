package extensions.order;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import services.common.UUIDGenerator;
import services.price.PriceCalculationContextFactory;
import services.price.PriceService;
import valueobjects.price.Price;
import valueobjects.price.PriceCalculationContext;
import valueobjects.product.ProductLite;
import valueobjects.product.spec.IProductSpec;
import valueobjects.product.spec.SingleProductSpec;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dto.order.OrderDetail;

public class DefaultOrderDetailProvider implements IOrderDetailProvider {
	@Inject
	private PriceService priceService;
	@Inject
	private PriceCalculationContextFactory factory;

	@Override
	public String getId() {
		return "default";
	}

	@Override
	public List<OrderDetail> createOrderDetails(
			Map<String, ProductLite> productMap, Map<String, Integer> qtyMap,
			String currency, int siteID, int lang, String email) {
		PriceCalculationContext context = factory.create(currency);
		List<String> listingIDs = Lists.newArrayList(qtyMap.keySet());
		List<IProductSpec> specs = Lists.transform(listingIDs, id -> {
			Integer qty = qtyMap.get(id);
			if (qty != null) {
				return new SingleProductSpec(id, qty);
			}
			return null;
		});
		specs = Lists.newArrayList(Collections2.filter(specs, s -> s != null));
		List<Price> prices = priceService.getPrice(specs, context);
		List<OrderDetail> details = Lists.transform(prices, p -> {
			ProductLite product = productMap.get(p.getListingId());
			if (product == null) {
				return null;
			}
			OrderDetail detail = new OrderDetail();
			detail.setCid(UUIDGenerator.createAsString());
			detail.setClistingid(p.getListingId());
			detail.setCsku(product.getSku());
			detail.setCtitle(product.getTitle());
			detail.setForiginalprice(p.getUnitBasePrice());
			detail.setFprice(p.getUnitPrice());
			detail.setFtotalprices(p.getPrice());
			detail.setIqty(p.getQuantity());
			detail.setFweight(product.getWeight());
			return detail;
		});
		return Lists.newArrayList(Collections2.filter(details, d -> d != null));
	}
}
