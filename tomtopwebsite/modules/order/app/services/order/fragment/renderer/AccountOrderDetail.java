package services.order.fragment.renderer;

import java.util.List;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.order.IBillDetailService;
import services.order.IOrderService;
import valueobjects.order_api.OrderItem;
import dto.Currency;
import dto.order.BillDetail;
import dto.order.Order;

public class AccountOrderDetail {
	@Inject
	IBillDetailService billDetailService;
	@Inject
	IOrderService orderService;

	public Html render(Order order, Currency currency) {
		List<OrderItem> orderList = orderService.getOrderDetailByOrder(order);

		List<BillDetail> bills = billDetailService.getExtraBill(order.getIid());
		return views.html.order.member.order_product_view.render(orderList,
				order, currency, false, bills);
	}
}
