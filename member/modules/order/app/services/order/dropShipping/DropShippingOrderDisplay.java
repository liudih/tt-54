package services.order.dropShipping;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.utils.DoubleCalculateUtils;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderPackService;
import services.payment.IPaymentService;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderList;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import dto.Currency;
import dto.order.Order;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import extensions.order.collect.ICollectProvider;
import extensions.payment.IPaymentProvider;

public class DropShippingOrderDisplay {
	@Inject
	IOrderService orderService;

	@Inject
	CurrencyService currencyService;

	@Inject
	IOrderStatusService statusService;

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
		Multimap<String,Order> orderMul = Multimaps.index(orders, o -> o.getCdropshippingid());
		List<OrderItem> allDetails = Lists.newArrayList();	//所有的订单详情 
		for(String dropid : orderMul.keySet()){
			List<Order> olist = Lists.newArrayList(orderMul.get(dropid));
			if(olist.size()>0){
				OrderList ol = new OrderList();
				ol.setOrderItems(Lists.newArrayList());
				ol.setOrders(Lists.newArrayList());
				DoubleCalculateUtils duti = new DoubleCalculateUtils(0.0);
				for(Order o : olist){
					List<OrderItem> items = orderService.getOrderDetailByOrder(o);
					ol.getOrderItems().addAll(items);
					allDetails.addAll(items);
					duti = duti.add(o.getFgrandtotal());
					packMap.put(o.getIid(), packService.getByOrderId(o.getIid()));
				}
				ol.setOrders(olist);
				ol.setOrder(olist.get(0));
				ol.setGrandprice(duti.doubleValue());
				ol.setSymbol(currencyIndex.get(olist.get(0).getCcurrency()).getCsymbol());
				ol.setCurrency(currencyIndex.get(olist.get(0).getCcurrency()));
				ol.setDropshippingId(olist.get(0).getCdropshippingid());
				allItems.add(ol);
			}
		}
		Map<Integer, Collection<OrderItem>> itemmaps = Maps.newLinkedHashMap();
		if(allDetails!=null && allDetails.size()>0){
			Multimap<Integer,OrderItem> orderDetailMaps = Multimaps.index(allDetails, a -> a.getOrderid());
			itemmaps = orderDetailMaps.asMap();
		}
		
		Map<String, IPaymentProvider> payments = paymentService.getMap();
		Map<Integer, OrderStatus> idMap = statusService.getIdMap();
		return views.html.order.member.dropship.order_table_new.render(allItems, isShow,
				pageCount, pageNum, true, collectList, packMap, payments,
				idMap, type, itemmaps, currencyIndex);
	}

}