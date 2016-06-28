package extensions.interaction.member;

import javax.inject.Inject;

import play.Logger;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaciton.review.MemberReviewsServiece;
import services.member.login.LoginService;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberReviewsMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID = "reviews";

	@Inject
	MemberReviewsServiece memberReviewsServiece;

	@Inject
	LoginService loginService;

	@Inject
	FoundationService foundationService;

	@Override
	public String getCategory() {
		return "community";
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		Integer reviewCount = memberReviewsServiece
				.getTotalReviewsCountByMemberEmailAndSiteId(
						loginService.getLoginEmail(),
						foundationService.getSiteID());

		Logger.debug("reviews count======= " + reviewCount);
		final boolean highliht = ID.equals(currentMenuID);

		return views.html.interaction.member.menu_reviews.render(reviewCount,
				highliht);
	}

}
