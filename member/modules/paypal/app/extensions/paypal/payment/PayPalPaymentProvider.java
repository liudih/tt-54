package extensions.paypal.payment;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import mapper.order.DetailMapper;
import mapper.order.OrderMapper;
import play.Configuration;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.twirl.api.Html;
import services.order.OrderUpdateService;
import services.paypal.PaymentServices;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.paypal.PaypalPayment;
import dto.order.OrderDetail;
import enums.OrderLableEnum;
import extensions.payment.IPaymentProvider;

@Singleton
public class PayPalPaymentProvider implements IPaymentProvider {

	@Inject
	OrderMapper ordermapper;

	@Inject
	DetailMapper detailMapper;

	@Inject
	PaymentServices paymentservices;

	@Inject
	OrderUpdateService updateService;

	@Override
	public String id() {
		return "paypal";
	}

	@Override
	public String name() {
		return "PayPal";
	}

	@Override
	public int getDisplayOrder() {
		return 100;
	}

	@Override
	public String iconUrl() {
		return controllers.paypal.routes.Assets.at("/images/payment01.png")
				.url();
	}

	@Override
	public Html getDescription(PaymentContext context) {
		if (context == null || context.isModeNew()) {
			return views.html.paypal.v2.description.render();
		} else {
			return views.html.paypal.description.render();
		}

	}

	@Override
	public boolean isManualProcess() {
		return false;
	}

	@Override
	public Html getInstruction(PaymentContext context) {
		return null;
	}

	@Override
	public Html getDoPaymentHtml(PaymentContext context) {
		PaypalPayment payment = new PaypalPayment();
		payment.setReturnUrl(context.getBackUrl());
		payment.setOrder(context.getOrder().getOrder());
		payment.setOrderDetails(context.getOrder().getDetails());
		payment.setPaymentbase(paymentservices.GetPayment(context.getOrder()
				.getOrder()));
		Configuration config = Play.application().configuration()
				.getConfig("paypal");
		String Postaddress = "";
		if (config != null) {
			Boolean sandbox = config.getBoolean("sandbox");
			if (sandbox != null && sandbox) {
				Postaddress = "https://www.sandbox.paypal.com/cgi-bin/webscr";
			} else {
				Postaddress = "https://www.paypal.com/cgi-bin/webscr";
			}
		}
		Logger.debug("paypal post url is {}", Postaddress);
		payment.setPostaddress(Postaddress);
		updatePaymentAccount(context, payment);
		return views.html.paypal.gopaypal.render(payment);
	}

	private void updatePaymentAccount(PaymentContext context,
			PaypalPayment payment) {
		if (OrderLableEnum.DROP_SHIPPING.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, payment);
		} else if (OrderLableEnum.TOTAL_ORDER.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, payment);
		} else {
			updateAccountDefault(context, payment);
		}
	}

	private void updateAccountDefault(PaymentContext context,
			PaypalPayment payment) {
		updateService.updatePaymentAccount(context.getOrder().getOrder()
				.getIid(), payment.getPaymentbase().getCbusiness());
	}

	private void updateAccountTotal(PaymentContext context,
			PaypalPayment payment) {
		List<OrderDetail> details = context.getOrder().getDetails();
		for (OrderDetail detail : details) {
			updateService.updatePaymentAccount(detail.getIorderid(), payment
					.getPaymentbase().getCbusiness());
		}
	}

	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

	@Override
	public boolean validForm(DynamicForm df) {
		return false;
	}

	@Override
	public String area() {
		return "GLOBAL";
	}

}
