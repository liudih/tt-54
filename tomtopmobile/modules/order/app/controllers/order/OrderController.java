package controllers.order;

import java.util.List;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.lang3.StringUtils;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import plugins.mobile.order.OrderDetailPluginHelper;
import plugins.mobile.order.OrderFragmentPluginHelper;
import services.ICountryService;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import services.cart.ICartServices;
import services.loyalty.IPreferService;
import services.member.address.IAddressService;
import services.mobile.order.CartService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.exception.OrderException;
import valueobjects.base.LoginContext;
import valueobjects.loyalty.LoyaltyPrefer;
import valueobjects.order_api.CreateOrderRequest;
import valueobjects.order_api.OrderConfirmationRequest;
import valueobjects.order_api.OrderContext;
import valueobjects.cart.CartItem;
import valueobjects.order_api.payment.PaymentContext;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.Country;
import dto.member.MemberAddress;
import dto.order.Order;
import forms.order.PlaceOrder;
import forms.order.PlaceOrderV3;

/**
 * 
 * @author lijun
 *
 */
public class OrderController extends Controller {

	@Inject
	FoundationService foundationService;

	@Inject
	IOrderService orderService;

	@Inject
	IOrderStatusService statusService;

	@Inject
	OrderDetailPluginHelper pluginHelper;

	@Inject
	OrderFragmentPluginHelper helper;

	@Inject
	IAddressService addressService;

	@Inject
	ICountryService countryService;
	
	@Inject
	IPreferService prefer;
	
	@Inject
	ICartServices icartServices;
	
	public Result placeOrder() {

		Form<PlaceOrderV3> orderForm = Form.form(PlaceOrderV3.class)
				.bindFromRequest();
		if (orderForm.hasErrors()) {
			// XXX handle form error!
			return badRequest("form error: " + orderForm.errorsAsJson());
		}
		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		String currency = this.foundationService.getCurrency();
		webCtx.setCurrencyCode(currency);
		
		int site = this.foundationService.getSiteID();
		int lang = this.foundationService.getLanguage();
		String vhost = this.foundationService.getVhost();
		String origin = this.foundationService.getDevice();
		String ip = request().remoteAddress();

		PlaceOrderV3 porder = orderForm.get();
		// addressId在PlaceOrder中不是必须的所以要验证
		Integer addressId = porder.getAddressId();
		if (addressId == null) {
			Logger.debug("购物车生成order时addressId为空,所以强制用户到/cart");
			return redirect("/cart");
		}
		if (porder.getShipMethodCode() == null || "".equals(porder.getShipMethodCode())) {
			Logger.debug("购物车生成order时shippingMethodId为空,所以强制用户到/cart");
			return redirect("/cart");
		}
		Logger.debug("shipCode ==== " + porder.getShipMethodCode());

		try {
			List<CartItem> items = icartServices.getAllItemsCurrentStorageid(site, lang, currency);
			if (items == null || items.size() == 0) {
				Logger.debug("购物车没有商品重定向到/cart");
				return redirect("/cart");
			}
			List<LoyaltyPrefer> prefers = null;
			LoginContext loginCtx = this.foundationService.getLoginContext();
			String email = null;
			if (loginCtx != null && loginCtx.isLogin()) {
				email = loginCtx.getMemberID();
				prefers = prefer.getAllPreferByEmail(email, items, webCtx);
			}
			String message = porder.getMessage();
			Integer storageid = 1;
			String cstorageid = CookieUtils.getCookie("storageid", Context.current());
			if(cstorageid!=null && !"".equals(cstorageid) && StringUtils.isNumeric(cstorageid)){
				storageid = Integer.parseInt(cstorageid);
			}
			CreateOrderRequest request = new CreateOrderRequest(items, site,
					addressId, null, origin, message, ip, lang, currency, vhost,
					prefers, storageid);
			
			request.setShipCode(porder.getShipMethodCode());
			request.setShipMethodId(porder.getShipMethodId());
			
			Order order = this.orderService.createOrderInstance(request);
			
			//生成订单
			String ordernum = order.getCordernumber();
			if (ordernum != null) {
				icartServices.deleteItem(items);
				
				if (httpCtx.request().cookie("loyalty") != null) {
					CookieUtils.removeCookie("loyalty", httpCtx);
				}
				// 将订单对象传入优惠信息中
				if (null != prefers && prefers.size() > 0) {
					for (int i = 0; i < prefers.size(); i++) {
						prefers.get(i).setOrder(order);
					}
				}
					prefer.saveAllPrefer(email, prefers, webCtx);
			}
			// 如果用户使用优惠后导致付款金额为0或负值时,不能去付款
			Double grandtotal = order.getFgrandtotal();
			if (grandtotal == null || grandtotal <= 0) {
				return ok(views.html.mobile.order.pay.pay_failed.render(
						"Order total is invalid.",order.getCordernumber()));
			}
			/*OrderConfirmationRequest oreq = new OrderConfirmationRequest(
					porder.getCartId(), foundationService.getSiteID(),
					porder.getAddressId(), porder.getShippingMethodId(),
					origin, porder.getMessage(), ip,
					foundationService.getLanguage(),
					foundationService.getCurrency(),
					foundationService.getVhost());
			String corderId = orderService.confirmOrder(oreq);*/
			return redirect(controllers.order.routes.OrderController
					.viewOrder(ordernum));
		} catch (OrderException e) {
			Logger.debug("PlaceOrder Form: {}", porder);
			Logger.error("Save Error!", e);
			flash("error", e.getExceptionType().toString());
			return redirect("/cart");
		}
	}

	public Result viewOrder(String corderId) {
		PaymentContext paymentContext = orderService.getPaymentContext(
				corderId, foundationService.getLanguage());
		Order order = paymentContext.getOrder().getOrder();
		LoginContext loginCtx = foundationService.getLoginContext();
		if (order == null
				|| loginCtx == null
				|| !order.getCmemberemail().equals(loginCtx.getMemberID())
				|| statusService
						.getIdByName(IOrderStatusService.PAYMENT_PENDING) != order
						.getIstatus()) {
			return internalServerError("Not correct order");
		}
		if (order.getFshippingprice() == null) {
			return ok("This is a bad order,you need feedback to support staff");
		}

		return ok(views.html.mobile.order.orderDetail.render(order,
				pluginHelper));
	}

	/**
	 * 当用户更改邮寄地址后要刷新ShippingMethod, ajax 请求ShippingMethod
	 */
	public Result getShippingMethod(int addressId) {
		response().setContentType("text/html");
		MemberAddress address = addressService.getMemberAddressById(addressId);
		Integer countryId = address.getIcountry();
		Country country = foundationService.getCountry(countryId);
		if (country == null) {
			return badRequest();
		}
		Logger.debug("ajax 请求{} 国家 ShippingMethod", country.getCname());
		LoginContext loginCtx = foundationService.getLoginContext();
		String email = loginCtx.getMemberID();

		OrderContext orderCtx = new OrderContext(email, Lists.newArrayList());
		orderCtx.setCountry(country);

		Html result = helper.renderPlugin("shipping-method", orderCtx);
		return ok(result);
	}
}
