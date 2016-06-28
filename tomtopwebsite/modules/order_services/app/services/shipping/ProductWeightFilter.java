package services.shipping;

import java.util.List;

import javax.inject.Inject;

import services.product.ProductEnquiryService;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import valueobjects.product.Weight;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;

import extensions.order.shipping.IFreightPlugin;

public class ProductWeightFilter implements IFreightPlugin {
	@Inject
	private ProductEnquiryService productEnquiry;

	@Override
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {
		if (requst.getListingIds() == null || requst.getListingIds().isEmpty()) {
			return Lists.newArrayList();
		}
		List<Weight> weights = productEnquiry.getWeightList(requst
				.getListingIds());
		if (!FluentIterable.from(weights).filter(w -> w.getWeight() > 2000)
				.isEmpty()) {
			return Lists.newArrayList(FluentIterable.from(list).filter(
					s -> !s.getCode().contains("P")));
		}
		return list;
	}

	@Override
	public int order() {
		return 20;
	}

}
