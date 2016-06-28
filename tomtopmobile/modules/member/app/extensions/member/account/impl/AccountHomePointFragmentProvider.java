package extensions.member.account.impl;

import java.util.List;

import play.twirl.api.Html;

import com.google.common.collect.Lists;

import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

/**
 * 积分
 * 
 * @author lijun
 *
 */
public class AccountHomePointFragmentProvider implements
		IMemberAccountHomeFragmentProvider {
	// @Inject
	// IPointsService pointsService;
	//
	// @Inject
	// FoundationService foundationService;

	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.PROFILE);
	}

	@Override
	public int getDisplayOrder() {
		return 3;
	}

	@Override
	public Html getFragment(String email, Position position) {
		// int site = foundationService.getSiteID();
		// int useablePoint = pointsService.getUsefulPoints(email, site);

		return views.html.member.pointFragment.render();
	}
}
