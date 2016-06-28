package services.order;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import services.base.FoundationService;
import services.cart.ICartLifecycleService;
import services.member.login.LoginService;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.cart.CartGetRequest;
import dto.order.Order;
import dto.order.OrderDetail;
import facades.cart.Cart;

public class OrderContextUtil {

	@Inject
	Set<IOrderFragmentPlugin> fragmentPlugins;

	@Inject
	LoginService loginService;

	@Inject
	ICartLifecycleService cartService;

	@Inject
	FoundationService foundation;

	@Inject
	IOrderEnquiryService orderEnquiry;

	public OrderContext pretreatmentContext(OrderContext context) {
		for (IOrderFragmentPlugin fp : fragmentPlugins) {
			IOrderContextPretreatment pretreatment = fp
					.getContextPretreatment();
			if (pretreatment != null) {
				context = pretreatment.pretreatmentContext(context);
			}
		}
		return context;
	}

	public OrderContext createContext() {
		String memberEmail = loginService.getLoginData().getEmail();
		String ltc = foundation.getLoginContext().getLTC();
		CartGetRequest cgr = new CartGetRequest(memberEmail, ltc);
		Cart cart = cartService.getCart(cgr);
		if (null == cart || cart.getListingIDs().isEmpty()) {
			Logger.debug("cart is null? {}", cart == null);
			Logger.debug("CartGetRequest: {}", Json.toJson(cgr));
			return null;
		}
		OrderContext context = new OrderContext(memberEmail, cart);
		context.setSiteID(foundation.getSiteID());
		context.setLangID(foundation.getLanguage());
		context.setCurrency(foundation.getCountry());
		context = pretreatmentContext(context);
		return context;
	}

	public ExistingOrderContext createExstingOrderContext(String orderNumber,
			boolean isSelect) {
		Order order = orderEnquiry.getOrderById(orderNumber);
		List<OrderDetail> details = orderEnquiry.getOrderDetails(orderNumber);
		if (null == order || details.isEmpty()) {
			Logger.debug("Order number: {}, details size: {}", orderNumber,
					details.size());
			return null;
		}
		ExistingOrderContext context = new ExistingOrderContext(order, details,
				isSelect);
		context = pretreatExstingOrderContext(context);
		return context;
	}

	public ExistingOrderContext pretreatExstingOrderContext(
			ExistingOrderContext context) {
		for (IOrderFragmentPlugin fp : fragmentPlugins) {
			IOrderContextPretreatment pretreatment = fp
					.getContextPretreatment();
			if (pretreatment != null) {
				context = pretreatment.pretreatExstingOrderContext(context);
			}
		}
		return context;
	}

}
