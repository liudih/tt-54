package extensions.order.member;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.order.IOrderCountService;
import valueobjects.member.MemberInSession;
import valueobjects.order_api.OrderCount;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MemberOrderStatistics implements
		IMemberAccountHomeFragmentProvider {
	@Inject
	IOrderCountService countService;

	@Inject
	FoundationService foundation;

	@Override
	public Position getPosition() {
		return Position.STATISTICS;
	}

	@Override
	public int getDisplayOrder() {
		return 100;
	}

	@Override
	public Html getFragment(MemberInSession member) {
		String email = member.getEmail();
		OrderCount count = countService.getCountByEmail(email,
				foundation.getSiteID(), 1, false);
		return views.html.order.member.order_stat.render(count);
	}

}
