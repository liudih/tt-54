package handlers.order;

import play.Logger;
import services.order.OrderStatisticsService;
import services.product.CategoryEnquiryService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.order.AutoBundlingEvent;
import events.order.ProductsaleEvent;

public class ProductStatisticsHandler {

	@Inject
	OrderStatisticsService orderService;

	@Inject
	CategoryEnquiryService categoryEnquiryService;

	@Subscribe
	public void onAutoBundling(AutoBundlingEvent event) {
//		try {
//			orderService.bundProductByOrder(event.getSiteId());
//		} catch (Exception e) {
//			Logger.error("Bundle Product Error", e);
//		}
	}

	@Subscribe
	public void selectHotProduct(ProductsaleEvent event) {
		try {
			orderService.selectHotProduct(event.getSiteId());
		} catch (Exception e) {
			Logger.error("Select Hot Product Error", e);
		}
	}

}
