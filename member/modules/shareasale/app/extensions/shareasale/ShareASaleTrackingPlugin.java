package extensions.shareasale;

import javax.inject.Inject;

import dto.order.Order;
import play.twirl.api.Html;
import services.base.CurrencyService;
import extensions.payment.IPaymentHTMLPlugIn;

public class ShareASaleTrackingPlugin implements IPaymentHTMLPlugIn {
	@Inject
	CurrencyService currencyService;

	@Override
	public Html getHtml(Order order) {
		if ("shareasale".equals(order.getCorigin())) {
			double subtotal = currencyService.exchange(order.getFgrandtotal()
					- order.getFshippingprice(), order.getCcurrency(), "USD");
			return views.html.shareasale.tracking.render(order, subtotal);
		}
		return null;
	}

	@Override
	public int getDisplayOrder() {
		return 100;
	}

	@Override
	public int getType() {
		return WAIT_PAY;
	}

}
