package controllers.google;

import javax.inject.Inject;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import services.dodocool.member.MemberLoginService;
import services.member.IMemberEnquiryService;
import services.member.login.ILoginService;
import services.member.login.OtherLoginResult;
import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;
import dto.member.login.OtherLoginParameter;
import extensions.google.login.GoogleLoginProvider;

public class Google extends Controller {

	public static final String SOURCE = "google";

	public static final String ERROR = "error";
	public static final String ERROR_REASON = "error_reason";
	public static final String ERROR_DESCRIPTION = "error_description";

	public static final String TOKEN = "access_token";
	public static final String CODE = "code";
	public static final String STATE = "state";
	public static final String GRANTED_SCOPE = "granted_scopes";

	@Inject
	GoogleLoginProvider loginProvider;

	@Inject
	ILoginService loginService;

	@Inject
	MemberLoginService memberLoginService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	URLHelper urlHelper;

	public Promise<Result> returnFromGoogle() {
		Request request = request();
		String code = request.getQueryString(CODE);
		String state = request.getQueryString(STATE);
		String appId = Play.application().configuration()
				.getString("google.clientId");
		String appSecret = Play.application().configuration()
				.getString("google.clientSecret");
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		Logger.debug("code {}---------- state--{}  ", code, state);
		OtherLoginParameter param = new OtherLoginParameter(code, state,
				urlHelper.redirectUri());
		OtherLoginResult thirdLogin = loginService.thirdGoogleLogin(param,
				appId, appSecret, webContext);
		Logger.debug("login:----------------enum------{}",
				thirdLogin.getLenum());
		if (!thirdLogin.getLenum().toString().equals("not login")) {
			MemberBase memberBase = memberEnquiryService
					.getMemberByMemberEmail(thirdLogin.getEmail(), webContext);
			memberLoginService.forceLogin(memberBase);
			return Promise.pure(redirect(controllers.base.routes.Home.home()));
		}
		return Promise.pure(redirect(controllers.base.routes.Home.home()));
	}
}
