package authenticators.member;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.dodocool.base.FoundationService;
import valueobjects.member.MemberInSession;
import extensions.InjectorInstance;

public class MemberLoginAuthenticator extends play.mvc.Security.Authenticator {

	@Override
	public String getUsername(Context paramContext) {
		FoundationService foundationService = InjectorInstance.getInjector()
				.getInstance(FoundationService.class);
		MemberInSession mis = (MemberInSession) foundationService
				.getLoginservice().getPayload();
		Logger.debug("mis:{}", mis);
		if (mis == null) {
			return null;
		}
		return mis.getEmail();
	}

	@Override
	public Result onUnauthorized(Context paramContext) {
		String backUrl = paramContext.request().uri();
		Logger.debug("Redirecting to login form, backUrl={}", backUrl);
		return redirect(controllers.member.routes.Login.login());
	}
}
