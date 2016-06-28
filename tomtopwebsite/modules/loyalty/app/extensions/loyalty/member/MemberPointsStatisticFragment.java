package extensions.loyalty.member;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.loyalty.IPointsService;
import valueobjects.member.MemberInSession;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MemberPointsStatisticFragment implements
		IMemberAccountHomeFragmentProvider {

	@Inject
	IPointsService pointService;

	@Inject
	FoundationService foundation;

	@Override
	public Position getPosition() {
		return Position.STATISTICS;
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getFragment(MemberInSession member) {
		int points = pointService.getUsefulPoints(member.getEmail(),
				foundation.getSiteID());
		return views.html.loyalty.member.points.render(points);
	}

}
