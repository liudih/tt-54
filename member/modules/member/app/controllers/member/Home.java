package controllers.member;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.member.MemberBaseService;
import services.member.login.ILoginService;
import services.point.SigninPointService;
import valueobjects.member.MemberInSession;
import authenticators.member.MemberLoginAuthenticator;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;
import com.google.inject.Inject;

import context.ContextUtils;
import dto.Member;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class Home extends Controller {

	@Inject
	Set<IMemberAccountHomeFragmentProvider> homeFragments;

	@Inject
	MemberBaseService memberBaseService;

	@Inject
	ILoginService loginService;

	@Inject
	FoundationService foundationService;

	@Inject
	SigninPointService signinPointService;
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result index() {
		MemberInSession session = loginService.getLoginData();
		String email = session.getEmail();
		Member member = memberBaseService.getMember(email,ContextUtils.getWebContext(Context.current()));
		String account = member.getCaccount();

		if (account == null || "".equals(account)) {
			member.setCaccount(member.getCemail());
		}

		ListMultimap<Position, IMemberAccountHomeFragmentProvider> fragmentMaps = Multimaps
				.index(Ordering
						.natural()
						.onResultOf(
								(IMemberAccountHomeFragmentProvider p) -> p
										.getDisplayOrder())
						.sortedCopy(homeFragments), f -> f.getPosition());

		Map<Position, Collection<Html>> htmls = Multimaps.transformValues(
				fragmentMaps, p -> p.getFragment(session)).asMap();

		// for check user if sign today
		int siteId = foundationService.getSiteID();		
		boolean sign = signinPointService.checkMemberSignToday(email, siteId);		
		member.setBsign(sign);  //签到按钮是否灰色
		return ok(views.html.member.home.index.render(member, htmls));
	}

}
