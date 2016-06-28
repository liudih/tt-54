package controllers.paypal;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Result;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.paypal.UserInfo;
import controllers.member.LoginProviderControllerBase;
import extensions.paypal.login.PayPalLoginProvider;

public class PayPalLogin extends LoginProviderControllerBase {

	@Inject
	PayPalLoginProvider provider;

	@Inject
	URLHelper url;

	public final static String SOURCE = "paypal";

	public Promise<Result> returnFromPayPal() {
		String code = request().getQueryString("code");
		if (!StringUtils.isEmpty(code)) {
			Promise<UserInfo> userInfo = provider
					.getAccessToken(code, url.redirectUri())
					.flatMap(t -> provider.getUserInfo(t)).recover(t -> {
						Logger.error("PayPal Login Error", t);
						return null;
					});
			return userInfo
					.map(u -> {
						if (u != null) {
							MemberOtherIdentity otherId = new MemberOtherIdentity(
									SOURCE, u.getUserId(), u.getEmail());
							return loginOrRegistrationCompletion(otherId);
						}
						return redirect(controllers.member.routes.Login
								.loginForm("/"));
					});
		}
		Logger.debug("Code not returned from PayPal: {}", code);
		return Promise.pure(redirect(controllers.member.routes.Login
				.loginForm("/")));
	}

}
