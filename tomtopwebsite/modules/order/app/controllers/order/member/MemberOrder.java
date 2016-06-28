package controllers.order.member;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.utils.JsonFormatUtils;
import services.order.IOrderCountService;
import services.order.IOrderService;
import services.order.IOrderStatusService;
import services.order.OrderUpdateService;
import services.order.member.MemberOrderDisplay;
import services.order.member.MemberOrderProvider;
import services.order.member.OrderDetailDisplay;
import valueobjects.order_api.OrderCount;
import valueobjects.order_api.payment.PaymentContext;
import authenticators.member.MemberLoginAuthenticator;
import dto.order.Order;
import forms.order.MemberOrderForm;

@Authenticated(MemberLoginAuthenticator.class)
public class MemberOrder extends Controller {
	@Inject
	private FoundationService foundation;
	@Inject
	private OrderDetailDisplay display;
	@Inject
	private MemberOrderProvider provider;
	@Inject
	private IOrderCountService countService;
	@Inject
	private MemberOrderDisplay memberOrderDisplay;
	@Inject
	private OrderUpdateService updateService;
	@Inject
	private IOrderStatusService statusSerice;
	@Inject
	private IOrderService orderService;

	public Result index() {
		String email = foundation.getLoginContext().getMemberID();
		Integer siteId = foundation.getSiteID();
		OrderCount count = countService.getCountByEmail(email, siteId, 1, true);
		Html orders = memberOrderDisplay.getHtml(email, siteId, count, true);
		return ok(views.html.order.member.order_list.render(orders, count,
				"order-list", "my.order"));
	}

	public Result search() {
		Form<MemberOrderForm> orderForm = Form.form(MemberOrderForm.class)
				.bindFromRequest();
		if (orderForm.hasErrors()) {
			return badRequest("form error: " + orderForm.errorsAsJson());
		}
		MemberOrderForm form = orderForm.get();
		String email = foundation.getLoginContext().getMemberID();
		Integer siteId = foundation.getSiteID();
		List<Order> orders = provider.searchOrders(email, form, siteId, true);
		int pageCount = provider.searchOrderPage(email, form, siteId, true);
		return ok(display.getHtml(orders, form.getIsShow(), pageCount,
				form.getPageNum(), "order"));
	}

	public Result searchByTag() {
		Form<MemberOrderForm> orderForm = Form.form(MemberOrderForm.class)
				.bindFromRequest();
		if (orderForm.hasErrors()) {
			return badRequest("form error: " + orderForm.errorsAsJson());
		}
		MemberOrderForm form = orderForm.get();
		String email = foundation.getLoginContext().getMemberID();
		Integer siteId = foundation.getSiteID();
		String tag = form.getTag();
		List<Order> orders = provider.searchOrdersByTag(email, form, siteId,
				tag);
		int pageCount = provider.searchOrderPageByTag(email, form, siteId, tag);
		return ok(display.getHtml(orders, form.getIsShow(), pageCount,
				form.getPageNum(), "order"));
	}

	@SuppressWarnings("unchecked")
	public Result remove(String ids) {
		ArrayList<Integer> idList = JsonFormatUtils.jsonToBean(ids,
				ArrayList.class);
		String email = foundation.getLoginContext().getMemberID();
		updateService.updateShowValidEmail(
				OrderUpdateService.SHOW_TYPE_RECYCLE, idList, email);
		return ok();
	}

	@SuppressWarnings("unchecked")
	public Result delete(String ids) {
		ArrayList<Integer> idList = JsonFormatUtils.jsonToBean(ids,
				ArrayList.class);
		String email = foundation.getLoginContext().getMemberID();
		updateService.updateShowValidEmail(OrderUpdateService.SHOW_TYPE_FALSE,
				idList, email);
		return ok();
	}

	@SuppressWarnings("unchecked")
	public Result restore(String ids) {
		ArrayList<Integer> idList = JsonFormatUtils.jsonToBean(ids,
				ArrayList.class);
		String email = foundation.getLoginContext().getMemberID();
		updateService.updateShowValidEmail(OrderUpdateService.SHOW_TYPE_TURE,
				idList, email);
		return ok();
	}

	public Result completed(int id) {
		String email = foundation.getLoginContext().getMemberID();
		if (statusSerice.changeOrdeStatusValidEmailAndStatus(id,
				IOrderStatusService.COMPLETED, email,
				IOrderStatusService.DISPATCHED)) {
			return redirect(controllers.order.member.routes.MemberOrder.index());
		} else {
			return badRequest();
		}
	}

	public Result orderDetail(String id) {
		String email = foundation.getLoginContext().getMemberID();
		PaymentContext context = orderService.getPaymentContext(id,
				foundation.getLanguage());
		Order order = context.getOrder().getOrder();
		if (order == null) {
			return badRequest();
		}
		// modify by lijun
		if (email.equals(order.getCmemberemail())
				|| email.equals(order.getCemail())) {
			Html orderDetail = display.getOrderDetail(context);
			return ok(views.html.order.member.one_member_order
					.render(orderDetail));
		}
		return badRequest();
	}
	
}
