package extensions.interaction.member;

import javax.inject.Inject;

import mapper.interaction.InteractionCommentMapper;
import mapper.interaction.ProductCollectMapper;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.member.login.LoginService;
import valueobjects.member.MemberInSession;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MemberInteractionStatistics implements
		IMemberAccountHomeFragmentProvider {

	@Inject
	ProductCollectMapper productCollectMapper;

	@Inject
	InteractionCommentMapper memberReviewsMapper;
	
	@Inject
	FoundationService foundationService;
	
	

	@Override
	public Position getPosition() {
		return Position.STATISTICS;
	}

	@Override
	public int getDisplayOrder() {
		return 50;
	}

	@Override
	public Html getFragment(MemberInSession member) {
		int count = productCollectMapper.getCollectsCountByEmail(member
				.getEmail());
		int siteId = foundationService.getSiteID();
		int reviewCount = memberReviewsMapper
				.getTotalReviewsCountByMemberEmailAndSiteId(member.getEmail(),
						siteId);
		return views.html.interaction.member.stat.render(count, reviewCount);
	}

}
