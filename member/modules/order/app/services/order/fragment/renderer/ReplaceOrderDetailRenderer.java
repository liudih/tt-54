package services.order.fragment.renderer;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import play.twirl.api.Html;
import services.order.IBillDetailService;
import services.order.IOrderAlterHistoryService;
import services.order.IOrderService;
import valueobjects.order_api.OrderItem;
import dto.Currency;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderAlterHistory;
import dto.order.OrderDetail;

public class ReplaceOrderDetailRenderer {
	@Inject
	IOrderService orderService;
	@Inject
	IBillDetailService billDetailService;
	@Inject
	IOrderAlterHistoryService orderAlterHistoryService;

	public Html render(Order order, Currency currency) {
		List<OrderItem> items = orderService.getOrderDetailByOrder(order);
		List<BillDetail> bills = billDetailService.getExtraBill(order.getIid());
		OrderAlterHistory alterHistory = orderAlterHistoryService
				.getEarliestByOrder(order.getIid());
		Double discount = null;
		if (alterHistory != null) {
			double sum = order.getFordersubtotal() + order.getFshippingprice()
					+ order.getFextra();
			discount = sum - order.getFgrandtotal();
			if (discount == 0.0) {
				discount = null;
			}
		}
		return views.html.order.member.order_detail.render(items, order,
				currency, bills, discount);
	}

	public Html render(Order order, Currency currency, List<OrderItem> items) {
		Double discount = null;
		double sum = order.getFordersubtotal() + order.getFshippingprice()
				+ order.getFextra();
		discount = sum - order.getFgrandtotal();
		if (discount == 0.0) {
			discount = null;
		}
		return views.html.order.member.order_detail.render(items, order,
				currency, Lists.newArrayList(), discount);
	}

	public Html newRender(Order order, Currency currency,
			List<OrderDetail> details) {
		return views.html.order.payment.new_order_detail.render(order,
				currency, details);
	}

}
