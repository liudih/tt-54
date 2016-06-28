package extensions.order;

import java.util.List;

import javax.inject.Inject;

import dto.order.Order;
import dto.order.OrderDetail;
import play.libs.Json;
import play.twirl.api.Html;
import services.order.IOrderEnquiryService;
import extensions.payment.IPaymentHTMLPlugIn;

public class OrderInfoJSPlugIn implements IPaymentHTMLPlugIn {
	@Inject
	IOrderEnquiryService orderEnquiry;

	@Override
	public Html getHtml(Order order) {
		if (order != null) {
			List<OrderDetail> details = orderEnquiry.getOrderDetails(order
					.getIid());
			String detailsStr = Json.toJson(details).toString();
			String orderStr = Json.toJson(order).toString();
			return views.html.order.order_info_js.render(orderStr, detailsStr);
		}
		return Html.apply("");
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public int getType() {
		return PAY_SUCCESS;
	}

}
