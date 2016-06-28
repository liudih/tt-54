package controllers.mobile.about;

import interceptor.VisitLog;
import interceptor.auth.LoginAuth;
import interceptor.auth.TokenAuth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.ICurrencyService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.payment.IWebPaymentService;
import utils.CommonDefn;
import utils.ValidataUtils;
import valueobjects.order_api.payment.PaymentContext;
import valuesobject.mobile.BaseResultType;
import dto.Currency;
import dto.order.Order;
import dto.payment.PaymentPaypalParam;
import dto.payment.WebPaymentResult;

public class OrderPayController extends Controller {

	@Inject
	MobileService mobileService;
	@Inject
	IWebPaymentService paymentService;
	@Inject
	IOrderService orderService;
	@Inject
	LoginService loginService;
	@Inject
	IOrderStatusService statusService;
	@Inject
	ICurrencyService currencyService;

	/**
	 * 去支付页面
	 * 
	 * @param corderId
	 * 
	 * @return
	 */
	@With({ VisitLog.class, TokenAuth.class, LoginAuth.class })
	public Result payOrder(String corderId) {
		String email = loginService
				.getLoginMemberEmail(CommonDefn.ISLOGINTRUEEMAIL);
		Integer languageId = mobileService.getLanguageID();
		Map<String, Object> objMap = new HashMap<String, Object>();
		PaymentContext paymentContext = orderService.getPaymentContext(
				corderId, languageId);
		Order order = paymentContext.getOrder().getOrder();
		if (order == null) {
			objMap.put("re", BaseResultType.ORDER_PAY_NOT_FIND_ERROR_CODE);
			objMap.put("msg", BaseResultType.ORDER_PAY_NOT_FIND_ERROR_MSG);
			return ok(Json.toJson(objMap));
		} else {
			if (!ValidataUtils.validataStr(order.getCmemberemail()).equals(
					email)) {
				objMap.put("re",
						BaseResultType.ORDER_PAY_NOT_EQUALS_EMAIL_ERROR_CODE);
				objMap.put("msg",
						BaseResultType.ORDER_PAY_NOT_EQUALS_EMAIL_ERROR_MSG);
				return ok(Json.toJson(objMap));
			}
			Integer ost = statusService
					.getIdByName(IOrderStatusService.PAYMENT_PENDING);
			Integer status = order.getIstatus();
			if (ValidataUtils.validataInt(ost) != status) {
				objMap.put(
						"re",
						BaseResultType.ORDER_PAY_STATUS_NOT_PAYMENT_PENDING_ERROR_CODE);
				objMap.put(
						"msg",
						BaseResultType.ORDER_PAY_STATUS_NOT_PAYMENT_PENDING_ERROR_MSG);
				return ok(Json.toJson(objMap));

			}
		}
		Currency currency = paymentContext.getCurrency();
		if (currency == null) {
			objMap.put("re", BaseResultType.ORDER_PAY_CURRENCY_ERROR_CODE);
			objMap.put("msg", BaseResultType.ORDER_PAY_CURRENCY_ERROR_MSG);
			return ok(Json.toJson(objMap));
		}
		if (order.getFshippingprice() == null) {
			objMap.put("re", BaseResultType.ORDER_PAY_BAD_ERROR_CODE);
			objMap.put("msg", BaseResultType.ORDER_PAY_BAD_ERROR_MSG);
			return ok(Json.toJson(objMap));
		}
		PaymentPaypalParam param = new PaymentPaypalParam(corderId, languageId);
		WebPaymentResult paypalForm = null;
		try {
			String paypalReturnUrl = "http://app.tomtop.com/mobile/api/h5/fpay";
			// if (paypalReturnUrl == null || paypalReturnUrl.length() == 0) {
			// throw new NullPointerException(
			// "can not get payment_return_url config from application.conf");
			// }
			param.setReturnUrl(paypalReturnUrl + "?oid=" + corderId);
			paypalForm = paymentService.pay(param);
			// String pf = paypalForm.getRes();
			// String notifyUrl = Play.application().configuration()
			// .getString("paypal.notifyurl");
			// String appNotifyUrl =
			// "http://app.tomtop.com/mobile/api/paypal/payment";
			// pf = pf.replace(notifyUrl, appNotifyUrl);
			// paypalForm.setRes(pf);
		} catch (Exception e) {
			Logger.error("get paypal form error", e);
			e.printStackTrace();
			objMap.put("re", BaseResultType.EXCEPTION);
			objMap.put("msg", BaseResultType.EXCEPTIONMSG);
			return ok(Json.toJson(objMap));
		}

		return ok(views.html.mobile.orderDetail.render(order, currency,
				paypalForm));
	}

	/**
	 * 支付完成return页
	 * 
	 * @param corderId
	 * 
	 * @return
	 */
	public Result finishPay(String oid) {
		return ok(views.html.mobile.finish_paypal.render(oid));
	}
}
