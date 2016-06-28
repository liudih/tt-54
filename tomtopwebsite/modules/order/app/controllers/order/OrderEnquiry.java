package controllers.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.order.IOrderEnquiryService;
import services.order.IOrderPackService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.shipping.IShippingMethodService;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderList;
import valueobjects.order_api.OrderSummaryInfo;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.Currency;
import dto.ShippingMethodDetail;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import dto.order.OrderStatusHistory;
import dto.order.OrderTracking;

/**
 * 订单查询管理
 * 
 * @author Administrator
 *
 */
public class OrderEnquiry extends Controller {

	@Inject
	private IOrderEnquiryService iOrderEnquiryService;

	@Inject
	private IOrderStatusService iOrderStatusService;

	@Inject
	private IShippingMethodService iShippingMethodService;

	@Inject
	private IOrderPackService orderPackService;

	@Inject
	private IOrderService orderService;

	@Inject
	private CurrencyService currencyService;

	@Inject
	private FoundationService foundation;

	private Integer EMAIL_ERROR = 1;
	private Integer ORDERID_ERRORINTEGER = 2;
	private Integer SAVE_SUCCESS = 3;

	public Result orderEnquiry() {
		return ok(views.html.order.enquiry.order_enquiry.render());
	}

	public Result orderTrackingCheck() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<Order> orderrequestForm = Form.form(Order.class).bindFromRequest();
		Order form = orderrequestForm.get();
		if (orderrequestForm.hasErrors()) {
			return ok("error");
		}
		Order order = iOrderEnquiryService.getOrderById(form.getCordernumber());
		if (null != order) {
			if (form.getCemail().equals(order.getCemail())) {
				resultMap.put("errorMessages", SAVE_SUCCESS);
				return ok(Json.toJson(resultMap));
			} else {
				resultMap.put("errorMessages", EMAIL_ERROR);
				return ok(Json.toJson(resultMap));
			}
		} else {
			resultMap.put("errorMessages", ORDERID_ERRORINTEGER);
			return ok(Json.toJson(resultMap));
		}
	}

	public Result orderTrackingQuery() {
		// 默认跳转页面
		Html trackingHtml = views.html.order.enquiry.order_enquiry.render();
		Form<Order> orderrequestForm = Form.form(Order.class).bindFromRequest();
		Order form = orderrequestForm.get();
		if (orderrequestForm.hasErrors()) {
			return ok("error");
		}
		Order order = iOrderEnquiryService.getOrderById(form.getCordernumber());
		if (null != order) {
			if (form.getCemail().equals(order.getCemail())) {
				String email = order.getCemail();
				PaymentContext context = orderService.getPaymentContext(
						form.getCordernumber(), foundation.getLanguage());
				Map<String, OrderStatusHistory> map = iOrderStatusService
						.getOrderHistoryMap(context.getOrder().getOrder()
								.getIid());
				DateFormatUtils dateUtil = DateFormatUtils
						.getInstance("MM/dd/yyyy K:mm a");
				Map<Integer, OrderStatus> idMap = iOrderStatusService
						.getIdMap();
				Map<Integer, List<OrderPack>> packMap = Maps.newHashMap();
				packMap.put(order.getIid(),
						orderPackService.getByOrderId(order.getIid()));
				return ok(views.html.order.enquiry.track_order_detail.render(
						context, map, dateUtil, idMap, packMap));
			} else {
				Logger.debug("form.getCemail() == " + form.getCemail());
			}
		}
		return ok();
	}
}
