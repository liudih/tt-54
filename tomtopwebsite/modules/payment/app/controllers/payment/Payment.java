package controllers.payment;

import javax.inject.Inject;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.order.IOrderService;
import valueobjects.order_api.payment.PaymentContext;
import authenticators.member.MemberLoginAuthenticator;
import extensions.payment.impl.wiretransfer.WireTransferPaymentProvider;

@Authenticated(MemberLoginAuthenticator.class)
public class Payment extends Controller {
	@Inject
	private WireTransferPaymentProvider wireTransferProvider;
	@Inject
	private IOrderService orderService;
	@Inject
	private FoundationService foundation;

	public Result toWireTransfer() {
		DynamicForm form = Form.form().bindFromRequest();
		String orderId = null;
		try {
			orderId = form.get("orderId");
		} catch (Exception e) {
			Logger.debug("Payment Controller toWireTransfer: ", e);
			return badRequest("form error: orderId is not found");
		}
		PaymentContext context = orderService.getPaymentContext(orderId,
				foundation.getLanguage());
		return ok(views.html.payment.manual_process_payment
				.render(wireTransferProvider.getInstruction(context)));
	}
}
