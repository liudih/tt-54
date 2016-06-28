package controllers.manager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import jdk.nashorn.internal.ir.ForNode;
import dto.order.Order;
import dto.order.PaymentCallback;
import entity.payment.PaypaiReturnLog;
import forms.order.MemberOrderForm;
import forms.order.OrderForm;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.payment.IPaymentCallbackService;
import services.paypal.PaymentServices;

/**
 * 支付日志查询管理类
 * 
 * @author Guozy
 *
 */
public class PaymentLog extends Controller {

	@Inject
	private IOrderEnquiryService iOrderEnquiryService;

	@Inject
	private IOrderStatusService iStatusService;

	@Inject
	private PaymentServices paymentServices;

	@Inject
	private IPaymentCallbackService iPaymentCallbackService;

	// 时间间隔默认为零
	private final Integer DEFAULT_TIME_INTERVAL = 0;

	/**
	 * 初始化数据信息
	 * 
	 * @param pageNum
	 * @return
	 */
	public Result getInitPayments(int num) {
		MemberOrderForm form = new MemberOrderForm();
		form.setPageNum(num);

		return ok(views.html.manager.payment.payment_log_query.render(form,
				null, null, null, null, 0, 0, form.getPageNum()));
	};

	/**
	 * 根据相应条件，获取数据信息
	 * 
	 * @return
	 */
	public Result search() {
		Form<MemberOrderForm> form = Form.form(MemberOrderForm.class)
				.bindFromRequest();
		MemberOrderForm memberOrderForm = form.get();
		return ok(getPaymentLogs(memberOrderForm));
	}

	/**
	 * 获取
	 * 
	 * @return
	 */
	public Html getPaymentLogs(MemberOrderForm form) {
		// 设置时间默认为零
		form.setInterval(DEFAULT_TIME_INTERVAL);
		// 获取数据的说有信息
		List<Order> orders = iOrderEnquiryService.searchOrders(null, form,
				null, false);

		// 获取数据信息的数量
		Integer count = iOrderEnquiryService.searchOrderCount(null, form, null,
				false);
		Map<Integer, String> statusNameMap = new HashMap<Integer, String>();
		Map<Integer, List<PaypaiReturnLog>> paypaiReturnLogsMap = new HashMap<Integer, List<PaypaiReturnLog>>();
		Map<Integer, List<PaymentCallback>> paymentCallbacksMap = new HashMap<Integer, List<PaymentCallback>>();
		String istatusName = null;
		for (Order order : orders) {
			istatusName = iStatusService.getOrderStatusNameById(order
					.getIstatus());
			statusNameMap.put(order.getIid(), istatusName);
			List<PaypaiReturnLog> paymentLogList = paymentServices
					.getPaypaiReturnLogByOrderIds(order.getIid()+"");
			paypaiReturnLogsMap.put(order.getIid(), paymentLogList);
			List<PaymentCallback> paymentCallbackList = iPaymentCallbackService
					.getByOrderNumerAndSiteID(order.getCordernumber(), null);
			paymentCallbacksMap.put(order.getIid(), paymentCallbackList);
		}
		Integer pageTotal = count / form.getPageSize()
				+ ((count % form.getPageSize() > 0) ? 1 : 0);

		return views.html.manager.payment.payment_log_query.render(form,
				paymentCallbacksMap, paypaiReturnLogsMap, statusNameMap,
				orders, count, pageTotal, form.getPageNum());
	}
}
