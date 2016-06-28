package extensions.loyalty.member;

import java.util.List;

import com.google.inject.Inject;

import dto.MemberLevel;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.member.MemberGroupCriterionService;
import services.member.MemberLevelService;
import services.member.login.LoginService;
import valueobjects.member.MemberInSession;
import dto.member.MemberGroupCriterion;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MemberLevelProfileFragment implements
		IMemberAccountHomeFragmentProvider {

	@Inject
	LoginService loginService;

	@Inject
	FoundationService foundationService;

	@Inject
	MemberLevelService memberLevelService;

	@Override
	public Position getPosition() {
		return Position.PROFILE;
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getFragment(MemberInSession member) {
		MemberInSession session = loginService.getLoginData();
		String email = session.getEmail();
		int websiteid = foundationService.getSiteID();
		MemberLevel memberlevel = memberLevelService.getMemberLevel(websiteid,
				email);

		return views.html.loyalty.member.level.render(memberlevel);
	}

}
