package controllers.order;

import play.mvc.Controller;
import play.mvc.Result;

public class PaymentControllerBase extends Controller {

	protected Result redirectToPaymentConfirmation(String orderId) {
		return redirect(routes.OrderProcessing.paymentConfirmed(orderId));
	}

	protected Result redirectToPayFail(String orderId, String result) {
		return redirect(routes.OrderProcessing.paymentFail(orderId, result, ""));
	}

}
