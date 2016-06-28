package controllers.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import play.data.DynamicForm.Dynamic;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.WebsiteService;
import services.order.IBillDetailService;
import services.order.IOrderAlterHistoryService;
import services.order.IOrderEnquiryService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderPackService;
import services.order.OrderUpdateService;
import services.payment.IPaymentService;
import services.shipping.ShippingMethodService;
import services.wholesale.WholeSaleDiscountLevelEnquiryService;
import services.wholesale.WholeSaleDiscountLevelUpdateService;
import session.ISessionService;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderList;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dto.Currency;
import dto.Website;
import dto.order.BillDetail;
import dto.order.Order;
import dto.order.OrderAlterHistory;
import dto.order.OrderPack;
import dto.order.OrderStatus;
import entity.manager.AdminUser;
import entity.wholesale.WholeSaleDiscountLevel;
import extensions.payment.IPaymentProvider;
import forms.WholeSaleDiscountForm;
import forms.order.MemberOrderForm;
import forms.order.OrderAlterHistoryForm;
import forms.order.OrderForm;

public class WholeSale extends Controller {
	@Inject
	private IOrderStatusService orderStatusService;
	@Inject
	private IOrderEnquiryService orderEnquiryService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private IOrderService orderService;
	@Inject
	private IBillDetailService billDetailService;
	@Inject
	private OrderPackService packService;
	@Inject
	private IPaymentService paymentService;
	@Inject
	private IOrderStatusService statusService;
	@Inject
	private ShippingMethodService shippingMethodService;
	@Inject
	private ISessionService sessionService;
	@Inject
	private IOrderAlterHistoryService orderAlterHistoryService;
	@Inject
	private OrderUpdateService orderUpdateService;
	@Inject
	private WholeSaleDiscountLevelEnquiryService discountLevelEnquiry;
	@Inject
	private FoundationService foundation;
	@Inject
	private WebsiteService websiteService;
	@Inject
	private WholeSaleDiscountLevelUpdateService discountLevelUpdate;

	final String label = "wholesale";

	public Result manageOrder() {
		Map<String, OrderStatus> nameMap = orderStatusService.getNameMap();
		return ok(views.html.manager.member.wholesaleorder.wholesale_order_manager
				.render(nameMap));
	}

	public Result search() {
		Form<OrderForm> orderForm = Form.form(OrderForm.class)
				.bindFromRequest();
		return ok(getOrderListHtml(orderForm.get()));
	}

	protected Html getOrderListHtml(OrderForm orderForm) {
		if (null == orderForm.getSiteId()) {
			return views.html.manager.member.wholesaleorder.order_table_list
					.render(null, 0, 0, 0, null, null, null, null, null,null);
		}
		List<Order> orders = orderEnquiryService.searchOrdersByLabel(orderForm,
				label);
		if (orders.size() == 0) {
			return views.html.manager.member.wholesaleorder.order_table_list
					.render(null, 0, 0, 0, null, null, null, null, null,null);
		}
		List<OrderList> allItems = Lists.newArrayList();
		List<String> currencys = Lists.transform(orders,
				list -> list.getCcurrency());
		List<Currency> curList = currencyService.getCurrencyByCodes(currencys);

		Map<String, Currency> currencyIndex = Maps.uniqueIndex(curList,
				i -> i.getCcode());
		Map<Integer, List<OrderPack>> packMap = Maps.newHashMap();
		Map<Integer, Double> weightMap = Maps.newHashMap();
		Map<Integer, String> orderMessage = Maps.newHashMap();
		for (Order ord : orders) {
			OrderList o = new OrderList();
			o.setOrder(ord);
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
		Integer orderCount = orderEnquiryService
				.searchCountByLabel(orderForm, label);
		Integer pageTotal = orderCount / orderForm.getPageSize()
				+ ((orderCount % orderForm.getPageSize() > 0) ? 1 : 0);
		Map<String, IPaymentProvider> payments = paymentService.getMap();
		Map<Integer, OrderStatus> idMap = statusService.getIdMap();

		return views.html.manager.member.wholesaleorder.order_table_list
				.render(allItems, orderCount, orderForm.getPageNum(), pageTotal,
						packMap, shippingMethodService, payments, idMap,
						weightMap,orderMessage);
	}

	public Result editTotal(int id) {
		Order order = orderEnquiryService.getOrderById(id);
		if (order == null) {
			return badRequest("Can't found the order");
		}
		Currency cy = currencyService.getCurrencyByCode(order.getCcurrency());
		double orginTotal = orderEnquiryService.getOrginOtal(id);
		WholeSaleDiscountLevel level = getDiscountRange(foundation.getSiteID(),
				orginTotal, cy.getCcode());
		if (level == null) {
			return ok("This order can't get discount");
		}
		Double min = level.getFmindiscount();
		Double max = level.getFmaxdiscount();
		order.setFordersubtotal(orginTotal);
		return ok(views.html.manager.member.wholesaleorder.update_grand_total
				.render(order.getIid(), orginTotal, cy.getCsymbol(), min, max));
	}

	public Result saveTotal() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		OrderAlterHistoryForm orderAlterForm = Form
				.form(OrderAlterHistoryForm.class).bindFromRequest().get();
		Double oldPrice = Double.parseDouble(orderAlterForm.getColdvalue());
		Integer orderId = Integer.parseInt(orderAlterForm.getCorderid());
		Order order = orderEnquiryService.getOrderById(orderId);
		if (order == null) {
			return badRequest("Can't found this order!");
		}
		double orginTotal = orderEnquiryService.getOrginOtal(orderId);
		WholeSaleDiscountLevel level = getDiscountRange(foundation.getSiteID(),
				order.getFordersubtotal(), order.getCcurrency());
		if (level == null) {
			return badRequest("This order can't get discount");
		}
		Double min = level.getFmindiscount();
		Double max = level.getFmaxdiscount();

		Double newPrice = Double.parseDouble(orderAlterForm.getCnewvalue());
		double discount = ((orginTotal - newPrice) / orginTotal);
		double discountDouble = new BigDecimal(discount).setScale(4,
				BigDecimal.ROUND_HALF_UP).doubleValue();
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		if (discountDouble > max || discountDouble < min) {
			result.put("result", false);
		} else {
			HashMap<String, Double> oldValue = new HashMap<String, Double>();
			oldValue.put("changePrice", oldPrice);
			HashMap<String, Double> newValue = new HashMap<String, Double>();
			newValue.put("changePrice", newPrice);
			OrderAlterHistory orderAlterHistory = new OrderAlterHistory();
			orderAlterHistory.setCcreateuser(user.getCusername());
			orderAlterHistory.setDcreatedate(new Date());
			orderAlterHistory.setColdvalue(Json.toJson(oldValue).toString());
			orderAlterHistory.setCnewvalue(Json.toJson(newValue).toString());
			orderAlterHistory.setCorderid(orderAlterForm.getCorderid());
			orderAlterHistoryService.insertOrderAlterHistory(orderAlterHistory);
			boolean updateOrderPrice = orderUpdateService.updateOrderPrice(
					orderId, newPrice);
			result.put("result", updateOrderPrice);
		}

		return ok(Json.toJson(result));
	}

	public Result manageDiscount() {
		List<Website> sites = websiteService.getAll();
		return ok(views.html.manager.member.wholesale.discount_manage
				.render(sites));
	}

	public Result searchDiscount() {
		Dynamic form = Form.form().bindFromRequest().get();
		Integer siteID;
		try {
			siteID = Integer.valueOf(form.getData().get("siteID").toString());
		} catch (NumberFormatException e) {
			siteID = null;
		}
		if (siteID == null) {
			return badRequest();
		}
		List<WholeSaleDiscountLevel> list = discountLevelEnquiry
				.getBySite(siteID);
		return ok(views.html.manager.member.wholesale.discount_table
				.render(list));
	}

	public Result editDiscount(int id) {
		WholeSaleDiscountLevel d = discountLevelEnquiry.getByID(id);
		return ok(views.html.manager.member.wholesale.edit_discount.render(d));
	}

	public Result saveDiscount() {
		Form<WholeSaleDiscountForm> form = Form.form(
				WholeSaleDiscountForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest("form error: " + form.errorsAsJson());
		}
		WholeSaleDiscountForm discountForm = form.get();
		WholeSaleDiscountLevel level = new WholeSaleDiscountLevel();
		try {
			BeanUtils.copyProperties(level, discountForm);
		} catch (IllegalAccessException | InvocationTargetException e) {
			Logger.error("WholeSale saveDiscount: ", e);
		}
		boolean b = false;
		if (level.getFmindiscount() < level.getFmaxdiscount()
				&& level.getFmaxdiscount() < 100
				&& level.getFstartprice() < level.getFendprice()) {
			if (level.getIid() != null) {
				b = discountLevelUpdate.update(level);
			} else {
				b = discountLevelUpdate.insert(level);
			}
		}
		Map<String, Boolean> res = Maps.newHashMap();
		res.put("res", b);
		return ok(Json.toJson(res));
	}

	public Result deleteDiscount(int id) {
		boolean b = discountLevelUpdate.delete(id);
		if (b) {
			return ok();
		} else {
			return badRequest();
		}
	}

	private WholeSaleDiscountLevel getDiscountRange(int siteID, double price,
			String cy) {
		Double usdPrice = currencyService.exchange(price, cy, "USD");
		return discountLevelEnquiry.getBySiteAndPrice(siteID, usdPrice);
	}

}
