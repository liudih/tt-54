package authenticators.member;

import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.FoundationService;
import services.dropship.DropShipBaseEnquiryService;
import services.member.login.ILoginService;
import services.member.login.LoginService;
import valueobjects.member.MemberInSession;
import controllers.annotation.DropShippingAuthenticator;
import extensions.InjectorInstance;

public class DropShippingPermission extends Action<DropShippingAuthenticator> {

	@Override
	public Promise<Result> call(Context paramContext) throws Throwable {
		ILoginService loginService = InjectorInstance.getInjector()
				.getInstance(LoginService.class);
		MemberInSession mis = loginService.getLoginData(paramContext);
		if (mis != null) {
			String email = loginService.getLoginEmail();
			FoundationService foundation = InjectorInstance.getInjector()
					.getInstance(FoundationService.class);
			DropShipBaseEnquiryService dropShipBaseEnquiry = InjectorInstance
					.getInjector()
					.getInstance(DropShipBaseEnquiryService.class);
			boolean dropShipBase = dropShipBaseEnquiry
					.checkoutDropShipBaseByEmail(email, foundation.getSiteID());
			if (dropShipBase) {
				return delegate.call(paramContext);
			} else {
				return F.Promise
						.pure(redirect(controllers.dropship.routes.DropShip
								.join()));
			}
		} else {
			String backUrl = paramContext.request().uri();
			return F.Promise.pure(redirect(controllers.member.routes.Login
					.loginForm(backUrl)));
		}
	}
}
