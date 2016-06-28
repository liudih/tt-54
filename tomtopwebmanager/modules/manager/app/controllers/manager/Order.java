package controllers.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.IWebsiteService;
import services.base.WebsiteService;
import services.base.utils.ExcelUtils;
import services.base.utils.StringUtils;
import services.order.IOrderAlterHistoryService;
import services.order.IOrderEnquiryService;
import services.order.IOrderStatusService;
import services.order.OrderServices;
import services.order.OrderUpdateService;
import services.payment.PaymentConfirmationService;
import services.paypal.PaymentServices;
import services.shipping.IShippingMethodService;
import session.ISessionService;
import valueobjects.order_api.DropShipOrderMessage;
import valueobjects.order_api.OrderValue;

import com.google.common.eventbus.EventBus;

import controllers.InterceptActon;
import dto.Website;
import dto.order.OrderAlterHistory;
import dto.order.OrderStatus;
import entity.manager.AdminUser;
import entity.payment.PaypaiReturn;
import events.order.OrderCancelledEvent;
import forms.order.MemberOrderForm;
import forms.order.OrderAlterHistoryForm;
import forms.order.OrderForm;

@With(InterceptActon.class)
public class Order extends Controller {
	@Inject
	private OrderServices orderServices;
	@Inject
	private IOrderStatusService orderStatusService;
	@Inject
	private ISessionService sessionService;
	@Inject
	private OrderUpdateService orderUpdateService;
	@Inject
	private WebsiteService websiteEnquiryService;
	@Inject
	private PaymentServices paymentServices;
	@Inject
	private IOrderEnquiryService orderEnquiryService;
	@Inject
	private IShippingMethodService shippingMethodService;
	@Inject
	private PaymentConfirmationService paymentConfirmationService;
	@Inject
	private IOrderAlterHistoryService orderAlterHistoryService;
	@Inject
	private EventBus eventBus;
	@Inject
	private IWebsiteService iWebsiteService;

	final String label = "wholesale";

	public Result getOrderManager() {
		MemberOrderForm form = new MemberOrderForm();
		form.setPageSize(30);
		form.setSiteId(websiteEnquiryService.getDefaultWebsite().getIid());
		Map<String, OrderStatus> nameMap = orderStatusService.getNameMap();
		List<Website> websites = iWebsiteService.getAll();
		return ok(views.html.manager.order.order_manager.render(websites, null,
				nameMap));
	}

	public Html getOrderList(OrderForm orderForm) {
		return orderServices.getOrderList(orderForm);
	}

	public Result search() {
		Form<OrderForm> orderForm = Form.form(OrderForm.class)
				.bindFromRequest();
		return ok(getOrderList(orderForm.get()));
	}

	public Result updateOrderStatus(Integer orderid, Integer statusid,
			String transactionId) {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		Integer srcStatus = orderEnquiryService
				.getOrderStatusByOrderId(orderid);
		String srcStatusName = orderStatusService
				.getOrderStatusNameById(srcStatus);
		String nowStatusName = orderStatusService
				.getOrderStatusNameById(statusid);
		boolean updateTransactionId = orderUpdateService.updateTransactionId(
				orderid, transactionId);
		HashMap<String, String> oldValue = new HashMap<String, String>();
		oldValue.put("changeOrderStatus", srcStatusName);
		HashMap<String, String> newValue = new HashMap<String, String>();
		newValue.put("changeOrderStatus", nowStatusName);
		OrderAlterHistory orderAlterHistory = new OrderAlterHistory();
		orderAlterHistory.setCcreateuser(user.getCusername());
		orderAlterHistory.setDcreatedate(new Date());
		orderAlterHistory.setColdvalue(Json.toJson(oldValue).toString());
		orderAlterHistory.setCnewvalue(Json.toJson(newValue).toString());
		orderAlterHistory.setCorderid(orderid.toString());

		boolean changeOrdeStatus = orderStatusService
				.changeOrdeStatusInBackstage(orderid, statusid);
		if (changeOrdeStatus) {
			orderAlterHistoryService.insertOrderAlterHistory(orderAlterHistory);
		}
		if (statusid.equals(orderStatusService
				.getIdByName(IOrderStatusService.PAYMENT_CONFIRMED))
				&& changeOrdeStatus) {
			paymentConfirmationService.confirmPayment(orderid, transactionId);
		}
		if (statusid.equals(orderStatusService
				.getIdByName(IOrderStatusService.ORDER_CANCELLED))
				&& changeOrdeStatus) {
			OrderValue value = orderEnquiryService.getOrderValue(orderid);
			if (value.getOrder() != null) {
				Integer ilanguageid = websiteEnquiryService.getWebsite(
						value.getOrder().getIwebsiteid()).getIlanguageid();
				eventBus.post(new OrderCancelledEvent(value, ilanguageid));
			}
		}
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", changeOrdeStatus);
		return ok(Json.toJson(result));
	}

	public Result updateOrderPrice() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		OrderAlterHistoryForm orderAlterForm = Form
				.form(OrderAlterHistoryForm.class).bindFromRequest().get();
		Double oldPrice = Double.parseDouble(orderAlterForm.getColdvalue());
		Integer orderId = Integer.parseInt(orderAlterForm.getCorderid());
		HashMap<String, Double> oldValue = new HashMap<String, Double>();
		oldValue.put("changePrice", oldPrice);
		Double newPrice = Double.parseDouble(orderAlterForm.getCnewvalue());
		HashMap<String, Double> newValue = new HashMap<String, Double>();
		newValue.put("changePrice", newPrice);
		OrderAlterHistory orderAlterHistory = new OrderAlterHistory();
		orderAlterHistory.setCcreateuser(user.getCusername());
		orderAlterHistory.setDcreatedate(new Date());
		orderAlterHistory.setColdvalue(Json.toJson(oldValue).toString());
		orderAlterHistory.setCnewvalue(Json.toJson(newValue).toString());
		orderAlterHistory.setCorderid(orderAlterForm.getCorderid());
		orderAlterHistoryService.insertOrderAlterHistory(orderAlterHistory);
		boolean updateOrderPrice = orderUpdateService.updateOrderPrice(orderId,
				newPrice);
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", updateOrderPrice);

		return ok(Json.toJson(result));
	}

	public Result updateOrderShippingPrice() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		OrderAlterHistoryForm orderAlterForm = Form
				.form(OrderAlterHistoryForm.class).bindFromRequest().get();
		Double oldPrice = Double.parseDouble(orderAlterForm.getCextravalue());
		Double oldShippingPrice = Double.parseDouble(orderAlterForm
				.getColdvalue());
		Integer orderId = Integer.parseInt(orderAlterForm.getCorderid());
		Double newShippingPrice = Double.parseDouble(orderAlterForm
				.getCnewvalue());
		Double nowPrice = oldPrice - oldShippingPrice + newShippingPrice;
		boolean updateOrderPrice = orderUpdateService.updateOrderShippingPrice(
				orderId, nowPrice, newShippingPrice);
		if (updateOrderPrice) {
			HashMap<String, Double> oldValue = new HashMap<String, Double>();
			oldValue.put("changeShippingPrice", oldPrice);
			HashMap<String, Double> newValue = new HashMap<String, Double>();
			newValue.put("changeShippingPrice", newShippingPrice);
			oldValue.put("changePrice", oldShippingPrice);
			newValue.put("changePrice", nowPrice);
			OrderAlterHistory orderAlterHistory = new OrderAlterHistory();
			orderAlterHistory.setCcreateuser(user.getCusername());
			orderAlterHistory.setDcreatedate(new Date());
			orderAlterHistory.setColdvalue(Json.toJson(oldValue).toString());
			orderAlterHistory.setCnewvalue(Json.toJson(newValue).toString());
			orderAlterHistory.setCorderid(orderAlterForm.getCorderid());
			orderAlterHistoryService.insertOrderAlterHistory(orderAlterHistory);
		}
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		result.put("result", updateOrderPrice);

		return ok(Json.toJson(result));
	}

	public Result getPaypalMessage(String ordernumber) {
		PaypaiReturn paypaiReturn = paymentServices
				.getPaypaiReturnByOrderId(ordernumber);
		return ok(views.html.manager.order.paypal_message.render(paypaiReturn));
	}

	/**
	 * 
	 * @Title: exportOrderList
	 * @Description: TODO(导出订单Excel)
	 * @param @param siteId
	 * @param @param paymentId
	 * @param @param status
	 * @param @param orderNumber
	 * @param @param email
	 * @param @param transactionId
	 * @param @param start
	 * @param @param end
	 * @param @param vhost
	 * @param @param queryType
	 * @param @return
	 * @return Result
	 * @throws
	 * @author yinfei
	 */
	public Result exportOrderList(String siteId, String paymentId,
			String status, String orderNumber, String email,
			String transactionId, String start, String end, String vhost,
			String paymentStart, String paymentEnd, String queryType, String code) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 最终要转化为Excel的集合
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		try {
			if (null == queryType || "".equals(queryType.trim())) {
				OrderForm form = new OrderForm();
				form.setEmail(email);
				form.setEnd(end);
				if (StringUtils.notEmpty(orderNumber)) {
					form.setOrderNumber(orderNumber);
				}
				if (StringUtils.notEmpty(siteId)) {
					form.setSiteId(Integer.parseInt(siteId));
				}
				if (StringUtils.notEmpty(status)) {
					form.setStatus(status);
				}
				form.setPaymentId(paymentId);
				form.setStart(start);
				form.setTransactionId(transactionId);
				form.setVhost(vhost);
				form.setPaymentStart(paymentStart);
				form.setPaymentEnd(paymentEnd);
				form.setCode(code);
				// 查询出的订单
				List<dto.order.Order> orders = orderServices
						.getEnquiryOrders(form);
				data = orderServices.getExportOrders(orders, siteId);
			}
			if ("dropshipOrder".equals(queryType)) {
				Form<OrderForm> dsorderForm = Form.form(OrderForm.class)
						.bindFromRequest();
				List<DropShipOrderMessage> dsorders = orderEnquiryService
						.searchDropShipOrderList(dsorderForm.get());
				List<dto.order.Order> orders = orderServices
						.transformDSOToOrder(dsorders);
				data = orderServices.getExportOrders(orders, siteId);
			}
			if ("wholesaleOrder".equals(queryType)) {
				Form<OrderForm> orderForm = Form.form(OrderForm.class)
						.bindFromRequest();
				List<dto.order.Order> orders = orderEnquiryService
						.searchOrderListByLabel(orderForm.get(), label);
				data = orderServices.getExportOrders(orders, siteId);
			}
		} catch (Exception e) {
			Logger.error("export order error", e);
		}
		// 转换为Excel,并输出
		String filename = "order-list-" + sdf.format(new Date()) + ".xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	}

	/**
	 * 
	 * @Title: exportOrderDetailsList
	 * @Description: TODO(导出订单详情Excel)
	 * @param @param siteId
	 * @param @param paymentId
	 * @param @param status
	 * @param @param orderNumber
	 * @param @param email
	 * @param @param transactionId
	 * @param @param start
	 * @param @param end
	 * @param @param vhost
	 * @param @param paymentStart
	 * @param @param paymentEnd
	 * @param @param queryType
	 * @param @return
	 * @return Result
	 * @throws
	 * @author yinfei
	 */
	public Result exportOrderDetailsList(String siteId, String paymentId,
			String status, String orderNumber, String email,
			String transactionId, String start, String end, String vhost,
			String paymentStart, String paymentEnd, String queryType, String code) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 最终要转化为Excel的集合
		ArrayList<ArrayList<Object>> data = new ArrayList<ArrayList<Object>>();
		try {
			if (null == queryType || "".equals(queryType.trim())) {
				OrderForm form = new OrderForm();
				form.setEmail(email);
				form.setEnd(end);
				if (StringUtils.notEmpty(orderNumber)) {
					form.setOrderNumber(orderNumber);
				}
				if (StringUtils.notEmpty(siteId)) {
					form.setSiteId(Integer.parseInt(siteId));
				}
				if (StringUtils.notEmpty(status)) {
					form.setStatus(status);
				}
				form.setPaymentId(paymentId);
				form.setStart(start);
				form.setTransactionId(transactionId);
				form.setVhost(vhost);
				form.setPaymentStart(paymentStart);
				form.setPaymentEnd(paymentEnd);
				form.setCode(code);
				// 查询出的订单
				List<dto.order.Order> orders = orderServices
						.getEnquiryOrders(form);
				data = orderServices.getExportOrdersDetails(orders, siteId);
			}
			if ("dropshipOrder".equals(queryType)) {
				Form<OrderForm> dsorderForm = Form.form(OrderForm.class)
						.bindFromRequest();
				List<DropShipOrderMessage> dsorders = orderEnquiryService
						.searchDropShipOrderList(dsorderForm.get());
				List<dto.order.Order> orders = orderServices
						.transformDSOToOrder(dsorders);
				data = orderServices.getExportOrdersDetails(orders, siteId);
			}
			if ("wholesaleOrder".equals(queryType)) {
				Form<OrderForm> orderForm = Form.form(OrderForm.class)
						.bindFromRequest();
				List<dto.order.Order> orders = orderEnquiryService
						.searchOrderListByLabel(orderForm.get(), label);
				data = orderServices.getExportOrdersDetails(orders, siteId);
			}
		} catch (Exception e) {
			Logger.error("export order details error", e);
		}
		// 转换为Excel,并输出
		String filename = "order-list-" + sdf.format(new Date())
				+ "-details.xlsx";
		ExcelUtils excel = new ExcelUtils();
		byte[] tmpFile = excel.arrayToXLSX(data);
		response().setHeader("Content-disposition",
				"attachment; filename=" + filename);
		return ok(tmpFile).as("application/vnd.ms-excel");
	}

	public Result getPaymentConfirm(Integer orderid, Integer statusid,
			String ctransactionid) {
		return ok(views.html.manager.order.update_order_payment_confirm.render(
				orderid, statusid, ctransactionid));
	};
}
