package extensions.interaction.member;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaciton.review.MemberReviewsServiece;
import valueobjects.base.Page;
import valueobjects.interaction.ReviewsInMemberCenter;
import valueobjects.member.MemberInSession;

import com.google.inject.Inject;

import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MemberReviewsFragment implements
		IMemberAccountHomeFragmentProvider {

	@Inject
	MemberReviewsServiece memberReviewsServiece;

	@Inject
	FoundationService foudationService;

	@Override
	public Position getPosition() {
		return Position.BODY;
	}

	@Override
	public int getDisplayOrder() {
		return 19;
	}

	@Override
	public Html getFragment(MemberInSession member) {
		Integer totoal = 3;
		Integer page = 3;
		Integer limit = 3;
		Integer status = 3; // 3表示全部
		Integer dateType = 0;// 0表示all,1表示最近3个月，2表示最近6个月，3表示最近1年
		Integer siteId = foudationService.getSiteID();
		String email = member.getEmail();
		Page<ReviewsInMemberCenter> rePage = memberReviewsServiece
				.getMyReviewsPageByEmail(email, totoal, page, limit, status,
						dateType, siteId);
		return views.html.interaction.review.my_reviews.render(rePage);
	}
}
