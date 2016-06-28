package services.order.member;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.order.IOrderStatusService;
import valueobjects.order_api.OrderCount;
import dto.order.Order;
import dto.order.OrderStatus;
import forms.order.MemberOrderForm;

public class MemberOrderDisplay {
	@Inject
	MemberOrderProvider provider;

	@Inject
	OrderDetailDisplay display;

	@Inject
	IOrderStatusService orderStatusService;

	public Html getHtml(String email, Integer siteId, OrderCount count,
			boolean isNormal) {
		MemberOrderForm form = new MemberOrderForm();
		return getHtml(email, form, siteId, count, isNormal);
	}

	public Html getHtmlByTag(String email, Integer siteId, OrderCount count,
			String tag) {
		MemberOrderForm form = new MemberOrderForm();
		return getHtmlByTag(email, form, siteId, count, tag);
	}

	private Html getHtml(String email, MemberOrderForm form, Integer siteId,
			OrderCount count, boolean isNormal) {
		List<Order> orders = provider.searchOrders(email, form, siteId,
				isNormal);
		int pageCount = provider.searchOrderPage(email, form, siteId, isNormal);
		Html result = display.getHtml(orders, form.getIsShow(), pageCount,
				form.getPageNum(), "order");
		Map<String, OrderStatus> nameMap = orderStatusService.getNameMap();
		return views.html.order.member.order_display.render(result, count,
				nameMap, null);
	}

	private Html getHtmlByTag(String email, MemberOrderForm form,
			Integer siteId, OrderCount count, String tag) {
		List<Order> orders = provider.searchOrdersByTag(email, form, siteId,
				tag);
		int pageCount = provider.searchOrderPageByTag(email, form, siteId, tag);
		Html result = display.getHtml(orders, form.getIsShow(), pageCount,
				form.getPageNum(), "order");
		Map<String, OrderStatus> nameMap = orderStatusService.getNameMap();
		return views.html.order.member.tag_order_display.render(result, count,
				nameMap, tag);
	}

}
