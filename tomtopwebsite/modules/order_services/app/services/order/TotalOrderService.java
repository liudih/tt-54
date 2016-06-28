package services.order;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.libs.F;
import play.libs.F.Tuple;
import play.libs.F.Tuple3;
import services.base.CurrencyService;
import services.base.utils.DoubleCalculateUtils;
import valueobjects.order_api.ConfirmedOrder;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.collect.Lists;

import dao.order.ITotalOrderDao;
import dao.order.ITotalOrderMapDao;
import dto.Currency;
import dto.order.Order;
import dto.order.OrderDetail;
import dto.order.TotalOrder;
import dto.order.TotalOrderMap;

public class TotalOrderService {
	@Inject
	private ITotalOrderDao totalOrderDao;
	@Inject
	private ITotalOrderMapDao totalOrderMapDao;
	@Inject
	private OrderEnquiryService orderEnquiryService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private OrderService orderService;

	public String createTotalOrder(List<Order> orders) {
		if (!orders.isEmpty()) {
			String cid = orders.get(0).getCordernumber() + "-TT";
			totalOrderDao.insert(cid);
			TotalOrder totalOrder = totalOrderDao.getByCID(cid);
			List<TotalOrderMap> totalOrderMaps = Lists.transform(orders,
					order -> {
						TotalOrderMap map = new TotalOrderMap();
						map.setIorderid(order.getIid());
						map.setItotalid(totalOrder.getIid());
						return map;
					});
			totalOrderMapDao.batchInsert(totalOrderMaps);
			return cid;
		}
		return "";
	}

	public List<Integer> getOrderIDsByTotalCID(String cid) {
		TotalOrder totalOrder = totalOrderDao.getByCID(cid);
		List<Integer> list;
		if (totalOrder != null) {
			List<TotalOrderMap> maps = totalOrderMapDao.getByTotalID(totalOrder
					.getIid());
			list = Lists.transform(maps, map -> map.getIorderid());
		} else {
			list = Lists.newArrayList();
		}
		return list;
	}

	public List<Order> getOrdersByTotalCID(String cid) {
		List<Integer> ids = getOrderIDsByTotalCID(cid);
		List<Order> list = Lists.newArrayList();
		for (Integer id : ids) {
			Order order = orderEnquiryService.getOrderById(id);
			if (order != null) {
				list.add(order);
			}
		}
		return list;
	}

	public Tuple3<PaymentContext, List<OrderItem>, List<Order>> getPaymentContextAndOrder(
			String totalCID, int language) {
		List<Order> orders = getOrdersByTotalCID(totalCID);
		if (orders.isEmpty()) {
			return null;
		}
		List<OrderDetail> details = Lists.newArrayList();
		Order order = null;
		try {
			order = (Order) BeanUtils.cloneBean(orders.get(0));
		} catch (IllegalAccessException | InstantiationException
				| InvocationTargetException | NoSuchMethodException e) {
			Logger.error("BeanUtils.cloneBean() error: ", e);
		}
		DoubleCalculateUtils total = new DoubleCalculateUtils(0d);
		DoubleCalculateUtils freight = new DoubleCalculateUtils(0d);
		DoubleCalculateUtils discount = new DoubleCalculateUtils(0d);
		DoubleCalculateUtils subtotal = new DoubleCalculateUtils(0d);
		List<OrderItem> items = Lists.newArrayList();
		for (Order o : orders) {
			List<OrderItem> tempItems = orderService.getOrderDetailByOrder(o,
					language);
			items.addAll(tempItems);
			OrderDetail detail = new OrderDetail();
			detail.setIorderid(o.getIid());
			detail.setCid(o.getCordernumber());
			if (tempItems != null && !tempItems.isEmpty()) {
				detail.setCtitle(tempItems.get(0).getCtitle());
			} else {
				detail.setCtitle("Order Item " + o.getCordernumber());
			}
			detail.setCsku(o.getCordernumber());
			detail.setIqty(1);
			detail.setFtotalprices(o.getFordersubtotal());
			detail.setFprice(o.getFordersubtotal());
			details.add(detail);
			total = total.add(o.getFgrandtotal());
			freight = freight.add(o.getFshippingprice());
			discount = discount.add(o.getFextra());
			subtotal = subtotal.add(o.getFordersubtotal());
		}
		Currency currency = null;
		if (order != null) {
			currency = currencyService.getCurrencyByCode(order.getCcurrency());
			order.setCordernumber(totalCID);
			order.setFgrandtotal(total.doubleValue());
			order.setFextra(discount.doubleValue());
			order.setFordersubtotal(subtotal.doubleValue());
			order.setFshippingprice(freight.doubleValue());
		}
		ConfirmedOrder co = new ConfirmedOrder(order, details);
		PaymentContext context = new PaymentContext(co, null, currency);
		return F.Tuple3(context, items, orders);
	}

	public Tuple<PaymentContext, List<OrderItem>> getPaymentContext(
			String totalCID, int language) {
		Tuple3<PaymentContext, List<OrderItem>, List<Order>> t3 = getPaymentContextAndOrder(
				totalCID, language);
		return F.Tuple(t3._1, t3._2);
	}
}
