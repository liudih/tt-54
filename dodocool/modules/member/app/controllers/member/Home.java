package controllers.member;

import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.dodocool.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.login.ILoginService;
import valueobjects.member.MemberInSession;
import authenticators.member.MemberLoginAuthenticator;

import com.google.inject.Inject;

import context.ContextUtils;
import dto.member.MemberBase;

public class Home extends Controller {

	@Inject
	ILoginService loginService;

	@Inject
	IMemberEnquiryService memberService;

	@Inject
	FoundationService foundationService;
	
	@Authenticated(MemberLoginAuthenticator.class)
	public Result index() {
		MemberInSession loginservice = (MemberInSession) foundationService
				.getLoginservice().getPayload();
		String email = loginservice.getEmail();
		MemberBase member = memberService.getMemberByMemberEmail(email,
				ContextUtils.getWebContext(Context.current()));
		String account = member.getCaccount();

		if (account == null || "".equals(account)) {
			member.setCaccount(member.getCemail());
		}
		return ok(views.html.member.home.index.render(member));
	}

}
