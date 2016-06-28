package controllers.order;

import play.mvc.Controller;
import play.mvc.Result;
import services.base.FoundationService;
import services.order.IOrderService;
import services.order.OrderStatisticsService;

import com.google.inject.Inject;

public class Statistics extends Controller {
	@Inject
	OrderStatisticsService orderStatisticsService;

	@Inject
	IOrderService orderService;

	@Inject
	FoundationService foundation;

	public Result selectHot() {
		orderStatisticsService.selectHotProduct(foundation.getSiteID());
		return TODO;
	}

	public Result initListingid() {
		orderService.initListingid();
		return TODO;
	}

	public Result bundProductByOrder() {
		// orderStatisticsService.bundProductByOrder(foundation.getSiteID());
		return TODO;
	}
}
