package services.payment;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.data.DynamicForm;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.IOrderUpdateService;
import valueobjects.order_api.payment.PaymentContext;
import dto.order.Order;
import dto.payment.AbstractPaymentParam;
import dto.payment.WebPaymentResult;
import extensions.payment.IPaymentProvider;
import forms.order.ReplaceOrder;

public class WebPaymentService implements IWebPaymentService {
	@Inject
	private IOrderService orderService;
	@Inject
	private IPaymentService paymentService;
	@Inject
	private IOrderUpdateService orderUpdateService;
	@Inject
	private IOrderStatusService statusService;

	@Override
	public WebPaymentResult pay(AbstractPaymentParam param) {
		IPaymentProvider provider = paymentService.getPaymentById(param
				.getPaymentID());
		if (provider == null) {
			return new WebPaymentResult(new RuntimeException(
					"Can not found PaymentPlugin: " + param.getPaymentID()));
		}
		PaymentContext context = orderService.getPaymentContext(
				param.getOrderNumber(), param.getLangID());
		if (context == null) {
			return new WebPaymentResult(new RuntimeException(
					"Can not found order: " + param.getOrderNumber()));
		}
		//modify by lijun
		String backUrl = param.getReturnUrl();
		if(backUrl != null && backUrl.length() > 0){
			context.setBackUrl(backUrl);
		}
		
		if (provider.isNeedExtraInfo()) {
			Map<String, String> paramMap;
			DynamicForm df = new DynamicForm();
			try {
				paramMap = BeanUtils.describe(param);
				df.fill(paramMap);
				context.setMap(paramMap);
			} catch (IllegalAccessException | InvocationTargetException
					| NoSuchMethodException e) {
				Logger.error("OrderPaymentService getFormHtml Exception: ", e);
			}
			if (provider.validForm(df)) {
				return new WebPaymentResult(new RuntimeException("Form error: "
						+ df.errorsAsJson().toString()));
			}
		}
		savePaymentInfo(context.getOrder().getOrder(), param.getPaymentID());
		context.setBackUrl(backUrl);
		return new WebPaymentResult(provider.getDoPaymentHtml(context).body());
	}

	private void savePaymentInfo(Order order, String paymentID) {
		ReplaceOrder form = new ReplaceOrder();
		form.setCorderId(order.getCordernumber());
		form.setOrderId(order.getIid());
		form.setPaymentId(paymentID);
		Integer status = statusService
				.getIdByName(IOrderStatusService.PAYMENT_PENDING);
		orderUpdateService.replaceOrder(form, status, order.getCmemberemail());

	}
}
