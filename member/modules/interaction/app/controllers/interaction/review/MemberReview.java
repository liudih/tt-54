package controllers.interaction.review;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.FoundationService;
import services.interaciton.review.MemberReviewsServiece;
import services.member.login.LoginService;
import valueobjects.base.Page;
import valueobjects.interaction.ReviewsInMemberCenter;
import authenticators.member.MemberLoginAuthenticator;

import com.google.inject.Inject;

public class MemberReview extends Controller {

	@Inject
	MemberReviewsServiece memberReviewsServiece;

	@Inject
	LoginService loginservice;

	@Inject
	FoundationService foundationService;

	@Authenticated(MemberLoginAuthenticator.class)
	public Result reviewlist(Integer page, Integer limit, Integer status,
			Integer dateType, Integer tab) {
		String email = loginservice.getLoginData().getEmail();
		String userName = loginservice.getLoginData().getUsername();
		Integer siteId = foundationService.getSiteID();

		Integer totalReviewsCount = memberReviewsServiece
				.getTotalReviewsCountByMemberEmailAndSiteId(email, siteId);
		Integer PendingReviewsCount = memberReviewsServiece
				.getPendingReviewsCountByMemberEmailAndSiteId(email, siteId);
		Integer ApprovedReviewsCount = memberReviewsServiece
				.getApprovedReviewsCountByMemberEmailAndSiteId(email, siteId);
		Integer FailedReviewsCount = memberReviewsServiece
				.getFailedReviewsCountByMemberEmailAndSiteId(email, siteId);

		Page<ReviewsInMemberCenter> rePage = memberReviewsServiece
				.getMyReviewsPageByEmail(email, totalReviewsCount, page, limit,
						status, dateType, siteId);

		// TODO get Integer tomtopPoints
		return ok(views.html.interaction.review.my_reviews_list.render(rePage,
				userName, totalReviewsCount, PendingReviewsCount,
				ApprovedReviewsCount, FailedReviewsCount, status, dateType,
				email, tab));
	}

}
