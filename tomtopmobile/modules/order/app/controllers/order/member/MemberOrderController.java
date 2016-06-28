package controllers.order.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ICurrencyService;
import services.base.FoundationService;
import services.order.IBillDetailService;
import services.order.IOrderAlterHistoryService;
import services.order.IOrderCountService;
import services.order.IOrderEnquiryService;
import services.order.IOrderPackService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import valueobjects.order_api.OrderCount;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderList;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import controllers.MemberLoginValidate;
import dto.Currency;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderAlterHistory;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import dto.order.OrderStatusHistory;
import forms.order.MemberOrderForm;

public class MemberOrderController extends Controller {
	@Inject
	private FoundationService foundation;
	@Inject
	private IOrderCountService orderCountService;
	@Inject
	IOrderEnquiryService orderEnquiryService;
	@Inject
	ICurrencyService currencyService;
	@Inject
	IOrderService  orderService;
	@Inject
	IOrderStatusService orderStatusService;
	@Inject
	IOrderPackService orderPackService;
	@Inject
	IBillDetailService billDetailService;
	@Inject
	IOrderAlterHistoryService orderAlterHistoryService;
	

	@MemberLoginValidate
	public Result orderList(int status, int page, int isajax) {
		String email = foundation.getLoginContext().getMemberID();
		Integer siteId = foundation.getSiteID();
		int lang = foundation.getLanguage();
		OrderCount count = orderCountService.getCountByEmail(email, siteId, 1, true);
		
		MemberOrderForm mform = new MemberOrderForm();
		mform.setPageNum(page);
		mform.setPageSize(15);
		if(status==1){
			mform.setStatus(null);
		}else if(status==2){
			mform.setStatus(orderStatusService.getIdByName(IOrderStatusService.PAYMENT_PENDING));
		}else if(status==3){
			mform.setStatus(orderStatusService.getIdByName(IOrderStatusService.DISPATCHED));
		}else if(status==4){
			mform.setStatus(orderStatusService.getIdByName(IOrderStatusService.REFUNDED));
		}
		List<Order> orders = orderEnquiryService.searchOrders(email, mform, siteId, true);
		if(orders.size()==0){
			if(isajax==1){
				Map<String, Object> mjson = new HashMap<String,Object>();
				mjson.put("page", page);
				mjson.put("html","");
				return ok(Json.toJson(mjson));
			}else{
				return ok(views.html.mobile.order.member.order_list.render(count, status,
						Lists.newArrayList(), null, null));
			}
		}
		List<OrderList> allItems = Lists.newArrayList();
		List<String> currencys = Lists.transform(orders,
				list -> list.getCcurrency());
		List<Currency> curList = currencyService.getCurrencyByCodes(currencys);
		Map<String, Currency> currencyIndex = Maps.uniqueIndex(curList,
				i -> i.getCcode());
		Map<Integer, List<OrderPack>> packMap = Maps.newHashMap();
		for (Order ord : orders) {
			OrderList o = new OrderList();
			o.setOrder(ord);
			List<OrderItem> items = orderService.getOrderDetailByOrder(ord,lang);
			o.setSymbol(currencyIndex.get(ord.getCcurrency()).getCsymbol());
			o.setOrderItems(items);
			allItems.add(o);
			
			packMap.put(ord.getIid(), orderPackService.getByOrderId(ord.getIid()));
		}
		Map<Integer, OrderStatus> idMap = orderStatusService.getIdMap();
		if(isajax==1){
			StringBuilder sb = new StringBuilder("");
			for(OrderList o : allItems){
				sb.append(views.html.mobile.order.member.order_badge.render(o,idMap,packMap).toString());
			}
			Map<String, Object> mjson = new HashMap<String,Object>();
			mjson.put("page", page);
			mjson.put("html", sb.toString());
			return ok(Json.toJson(mjson));
		}
		
		return ok(views.html.mobile.order.member.order_list.render(count, status,
				allItems, idMap, packMap));
	}
	
	@MemberLoginValidate
	public Result orderDetail(String id){
		String email = foundation.getLoginContext().getMemberID();
		int lang = foundation.getLanguage();
		
		PaymentContext context = orderService.getPaymentContext(id,
				foundation.getLanguage());
		if (context == null
				|| context.getOrder() == null
				|| context.getOrder().getOrder() == null
				|| !context.getOrder().getOrder().getCmemberemail().equals(email)) {
			return badRequest();
		}
		Order order = context.getOrder().getOrder();
		List<OrderItem> orderList = orderService.getOrderDetailByOrder(order,lang);
		List<BillDetail> bills = billDetailService.getExtraBill(order.getIid());
		Map<Integer, OrderStatus> idMap = orderStatusService.getIdMap();
		List<OrderPack> tracklist = orderPackService.getByOrderId(order.getIid());
		Map<String, OrderStatusHistory> statusHistorymap = orderStatusService
				.getOrderHistoryMap(order.getIid());
		OrderStatusHistory dispatched = statusHistorymap.get(IOrderStatusService.DISPATCHED);
		Currency currency = currencyService.getCurrencyByCode(order.getCcurrency());
		String cur = currency==null ? "USD" : currency.getCsymbol(); 
		OrderAlterHistory alterHistory = orderAlterHistoryService
				.getEarliestByOrder(order.getIid());
		Double discount = null;
		if (alterHistory != null) {
			double sum = order.getFordersubtotal() + order.getFshippingprice()
					+ order.getFextra();
			discount = sum - order.getFgrandtotal();
			if (discount == 0.0) {
				discount = null;
			}
		}
		return ok(views.html.mobile.order.member.order_detail.render(orderList,
				order, cur, bills, idMap, 
				context, tracklist, statusHistorymap, dispatched, discount));
	}

	 
}
