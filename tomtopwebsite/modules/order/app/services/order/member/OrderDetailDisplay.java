package services.order.member;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.utils.DateFormatUtils;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderPackService;
import services.order.fragment.renderer.AccountOrderDetail;
import services.payment.IPaymentService;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderList;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.Currency;
import dto.order.Order;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import dto.order.OrderStatusHistory;
import extensions.order.collect.ICollectProvider;
import extensions.payment.IPaymentProvider;

public class OrderDetailDisplay {
	@Inject
	IOrderService orderService;

	@Inject
	CurrencyService currencyService;

	@Inject
	IOrderStatusService statusService;

	@Inject
	AccountOrderDetail orderDetailrender;

	@Inject
	Set<ICollectProvider> ICollectProviders;

	@Inject
	OrderPackService packService;

	@Inject
	IPaymentService paymentService;

	public Html getHtml(List<Order> orders, Integer isShow, Integer pageCount,
			Integer pageNum, String type) {
		List<ICollectProvider> collectList = Lists
				.newArrayList(ICollectProviders.iterator());
		pageNum = (pageNum == null) ? 1 : pageNum;
		if (orders.size() == 0) {
			return views.html.order.member.order_table.render(null, isShow,
					pageCount, pageNum, false, collectList, null, null, null,
					type);
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
			List<OrderItem> items = orderService.getOrderDetailByOrder(ord);
			o.setSymbol(currencyIndex.get(ord.getCcurrency()).getCsymbol());
			o.setCurrency(currencyIndex.get(ord.getCcurrency()));
			o.setOrderItems(items);
			allItems.add(o);

			packMap.put(ord.getIid(), packService.getByOrderId(ord.getIid()));
		}
		Map<String, IPaymentProvider> payments = paymentService.getMap();
		Map<Integer, OrderStatus> idMap = statusService.getIdMap();
		return views.html.order.member.order_table.render(allItems, isShow,
				pageCount, pageNum, true, collectList, packMap, payments,
				idMap, type);
	}

	public Html getOrderDetail(PaymentContext context) {
		Map<String, OrderStatusHistory> map = statusService
				.getOrderHistoryMap(context.getOrder().getOrder().getIid());
		Html orderDetail = orderDetailrender.render(context.getOrder()
				.getOrder(), context.getCurrency());
		Order order = context.getOrder().getOrder();
		IPaymentProvider payment = paymentService.getPaymentById(order
				.getCpaymentid());
		DateFormatUtils dateUtil = DateFormatUtils
				.getInstance("MM/dd/yyyy K:mm a");
		Map<Integer, OrderStatus> idMap = statusService.getIdMap();
		return views.html.order.member.one_order.render(context, map,
				orderDetail, payment, dateUtil, idMap);
	}
}
