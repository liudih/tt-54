package controllers.wholesale;

import javax.inject.Inject;

import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.order.OrderCountService;
import services.order.member.MemberOrderDisplay;
import valueobjects.order_api.OrderCount;

public class WholesaleOrder extends Controller {
	@Inject
	private FoundationService foundation;
	@Inject
	private OrderCountService countService;
	@Inject
	private MemberOrderDisplay memberOrderDisplay;

	public Result index() {
		String email = foundation.getLoginContext().getMemberID();
		Integer siteId = foundation.getSiteID();
		String tag = "wholesale";
		OrderCount count = countService.getCountByEmailAndTag(email, siteId, 1,
				tag);
		Html orders = memberOrderDisplay
				.getHtmlByTag(email, siteId, count, tag);
		return ok(views.html.order.member.order_list.render(orders, count,
				"wholesale-order", "wholesale.order"));
	}
}
