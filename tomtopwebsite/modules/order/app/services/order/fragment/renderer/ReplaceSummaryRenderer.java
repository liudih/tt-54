package services.order.fragment.renderer;

import java.util.Map;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.order.IBillDetailService;
import valueobjects.order_api.payment.PaymentContext;
import dto.order.BillDetail;

public class ReplaceSummaryRenderer {
	@Inject
	IBillDetailService billDetailService;

	public Html render(PaymentContext paymentContext) {
		Map<String, BillDetail> map = billDetailService
				.getMapExceptProduct(paymentContext.getOrder().getOrder()
						.getIid());
		return views.html.order.replace_summary.render(paymentContext, map);
	}
}
