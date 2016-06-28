package authenticators.member;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.member.login.ILoginService;
import services.member.login.LoginService;
import valueobjects.member.MemberInSession;
import extensions.InjectorInstance;

public class MemberLoginAuthenticator extends play.mvc.Security.Authenticator {

	@Override
	public String getUsername(Context paramContext) {
		ILoginService loginService = InjectorInstance.getInjector().getInstance(
				LoginService.class);
		MemberInSession mis = loginService.getLoginData(paramContext);
		if (mis == null) {
			return null;
		}
		return mis.getEmail();
	}

	@Override
	public Result onUnauthorized(Context paramContext) {
		String backUrl = paramContext.request().uri();
		Logger.debug("Redirecting to login form, backUrl={}", backUrl);
		return redirect(controllers.member.routes.Login.loginForm(backUrl));
	}
}
