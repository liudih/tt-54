package plugins.mobile.order;

import play.Logger;
import play.Play;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.payment.IWebPaymentService;

import com.google.inject.Inject;

import dto.order.Order;
import dto.payment.PaymentPaypalParam;
import dto.payment.WebPaymentResult;

/**
 * 付款方式
 * 
 * @author lijun
 *
 */
public class OrderPayentMethodPlugin implements IOrderDetailPlugin {
	private static final String NAME = "payent-method";

	@Inject
	FoundationService foundation;

	@Inject
	IWebPaymentService paymentService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Html render(Order order) {
		String number = order.getCordernumber();
		int language = foundation.getLanguage();
		PaymentPaypalParam param = new PaymentPaypalParam(number, language);
		WebPaymentResult paypalForm = null;
		try {
			String paypalReturnUrl = Play.application().configuration()
					.getString("payment_return_url");
			if (paypalReturnUrl == null || paypalReturnUrl.length() == 0) {
				throw new NullPointerException(
						"can not get payment_return_url config from application.conf");
			}
			param.setReturnUrl(paypalReturnUrl);
			paypalForm = paymentService.pay(param);
		} catch (Exception e) {
			Logger.error("get paypal form error", e);
		}

		return views.html.mobile.order.orderPaypal.render(order, paypalForm);
	}
}
