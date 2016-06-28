package controllers.manager;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import play.twirl.api.Html;
import services.ILanguageService;
import services.IWebsiteService;
import services.base.CurrencyService;
import services.order.IBillDetailService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderPackService;
import services.payment.IPaymentService;
import services.shipping.ShippingMethodService;
import valueobjects.order_api.DropShipOrderList;
import valueobjects.order_api.DropShipOrderMessage;
import valueobjects.order_api.OrderItem;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import controllers.InterceptActon;
import dto.Currency;
import dto.Website;
import dto.order.BillDetail;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import extensions.payment.IPaymentProvider;
import forms.order.DropShipOrderSearchForm;
import forms.order.OrderForm;

@With(InterceptActon.class)
public class DropShipOrder extends Controller {
	@Inject
	IOrderEnquiryService orderEnquiryService;
	@Inject
	IOrderStatusService orderStatusService;
	@Inject
	IOrderService orderService;
	@Inject
	CurrencyService currencyService;
	@Inject
	OrderPackService packService;
	@Inject
	ShippingMethodService shippingMethodService;
	@Inject
	IBillDetailService billDetailService;
	@Inject
	ILanguageService languageService;
	@Inject
	IPaymentService paymentService;
	@Inject
	IOrderStatusService statusService;

	public Result dropShipOrderManager() {
		Map<String, OrderStatus> nameMap = orderStatusService.getNameMap();
		return ok(views.html.manager.member.dropshiporder.dropship_order_manager
				.render(nameMap));
	}

	public Html getOrderListHtml(OrderForm form) {
		String rootPath = Play.application().configuration().getConfig("host").getString("link");
		if (rootPath!=null) {
			  rootPath=rootPath.substring(0, rootPath.length()-1);
		}
		if (null == form.getSiteId()) {
			return views.html.manager.member.dropshiporder.dropshiporder_table_list
					.render(rootPath,null, 0, 0, 0, null, null, null, null, null, null);
		}
		List<DropShipOrderMessage> orders = orderEnquiryService
				.searchDropShipOrders(form);
		if (orders.size() == 0) {
			return views.html.manager.member.dropshiporder.dropshiporder_table_list
					.render(rootPath,null, 0, 0, 0, null, null, null, null, null, null);
		}
		List<DropShipOrderList> allItems = Lists.newArrayList();
		List<String> currencys = Lists.transform(orders,
				list -> list.getCcurrency());
		List<Currency> curList = currencyService.getCurrencyByCodes(currencys);

		Map<String, Currency> currencyIndex = Maps.uniqueIndex(curList,
				i -> i.getCcode());
		Map<Integer, List<OrderPack>> packMap = Maps.newHashMap();
		Map<Integer, Double> weightMap = Maps.newHashMap();
		Map<Integer, String> orderMessage = Maps.newHashMap();
		for (DropShipOrderMessage ord : orders) {
			DropShipOrderList o = new DropShipOrderList();
			o.setDropShipOrderMessage(ord);
			List<OrderItem> items = orderService.getOrderDetailByOrder(ord);
			Double weight = 0.0;
			for (OrderItem orderItem : items) {
				if (null != orderItem.getWeight()) {
					weight += orderItem.getWeight() * orderItem.getIqty();
				}
			}
			List<BillDetail> extraBill = billDetailService.getExtraBill(ord
					.getIid());
			o.setOrderBillDetails(extraBill);
			o.setSymbol(currencyIndex.get(ord.getCcurrency()).getCsymbol());
			o.setOrderItems(items);
			packMap.put(ord.getIid(), packService.getByOrderId(ord.getIid()));
			weightMap.put(ord.getIid(), weight);
			allItems.add(o);
			orderMessage.put(ord.getIid(), ord.getCmessage());
		}
		Integer orderCount = orderEnquiryService.searchDropShipOrderCount(form);
		Integer pageTotal = orderCount / form.getPageSize()
				+ ((orderCount % form.getPageSize() > 0) ? 1 : 0);
		Map<String, IPaymentProvider> payments = paymentService.getMap();
		Map<Integer, OrderStatus> idMap = statusService.getIdMap();

		return views.html.manager.member.dropshiporder.dropshiporder_table_list
				.render(rootPath,allItems, orderCount, form.getPageNum(), pageTotal,
						packMap, shippingMethodService, payments, idMap,
						weightMap, orderMessage);
	}

	public Result search() {
		Form<OrderForm> orderForm = Form.form(OrderForm.class)
				.bindFromRequest();
		return ok(getOrderListHtml(orderForm.get()));
	}
}
