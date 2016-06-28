package extensions.wholesale;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Maps;

import services.common.UUIDGenerator;
import services.wholesale.WholesalePriceService;
import valueobjects.product.ProductLite;
import valueobjects.wholesale.PriceContext;
import valueobjects.wholesale.WholesalePrice;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dto.order.OrderDetail;
import extensions.order.IOrderDetailProvider;

public class WholesaleOrderDetailProvider implements IOrderDetailProvider {
	@Inject
	private WholesalePriceService service;

	@Override
	public String getId() {
		return "wholesale";
	}

	@Override
	public List<OrderDetail> createOrderDetails(
			Map<String, ProductLite> productMap, Map<String, Integer> qtyMap,
			String currency, int siteID, int lang, String email) {
		List<String> listingIDs = Lists.newArrayList(qtyMap.keySet());
		List<PriceContext> contexts = Lists.transform(listingIDs, id -> {
			Integer qty = qtyMap.get(id);
			ProductLite product = productMap.get(id);
			if (qty != null && product != null) {
				return new PriceContext(product.getSku(), qty);
			}
			return null;
		});
		contexts = Lists.newArrayList(Collections2.filter(contexts,
				c -> c != null));
		List<WholesalePrice> prices = service.getPrice(contexts, siteID);
		Map<String, WholesalePrice> priceMap = Maps.uniqueIndex(prices, p -> p
				.getPrice().getListingId());
		List<OrderDetail> details = Lists.transform(listingIDs, id -> {
			ProductLite product = productMap.get(id);
			WholesalePrice price = priceMap.get(id);
			if (product == null || price == null) {
				return null;
			}
			OrderDetail detail = new OrderDetail();
			detail.setCid(UUIDGenerator.createAsString());
			detail.setClistingid(id);
			detail.setCsku(product.getSku());
			detail.setCtitle(product.getTitle());
			detail.setForiginalprice(price.getPrice().getUnitBasePrice());
			detail.setFprice(price.getWsPrice());
			detail.setFtotalprices(price.getWsTotalPrice());
			detail.setIqty(price.getPrice().getQuantity());
			detail.setFweight(product.getWeight());
			return detail;
		});
		return Lists.newArrayList(Collections2.filter(details, d -> d != null));
	}
}
