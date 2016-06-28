package controllers.facebook;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Http.Request;
import play.mvc.Result;
import valueobjects.facebook.UserInfo;
import valueobjects.member.MemberOtherIdentity;
import controllers.member.LoginProviderControllerBase;
import extensions.facebook.login.FacebookLoginProvider;

public class Facebook extends LoginProviderControllerBase {

	public static final String SOURCE = "facebook";

	public static final String ERROR = "error";
	public static final String ERROR_REASON = "error_reason";
	public static final String ERROR_DESCRIPTION = "error_description";

	public static final String TOKEN = "access_token";
	public static final String CODE = "code";
	public static final String GRANTED_SCOPE = "granted_scopes";

	@Inject
	FacebookLoginProvider loginProvider;

	@Inject
	URLHelper urlHelper;

	public Promise<Result> returnFromFacebook() {
		Request request = request();
		String code = request.getQueryString(CODE);
		if (!StringUtils.isEmpty(code)) {
			// success, get further information from facebook
			Promise<UserInfo> userInfo = loginProvider
					.getAccessToken(code, urlHelper.redirectUri())
					.flatMap(token -> loginProvider.getUserInfo(token))
					.recover(t -> {
						Logger.error("Facebook Login Error", t);
						return null;
					});
			return userInfo
					.map(u -> {
						Logger.debug("Facebook UserInfo: {}", u);
						if (u != null) {
							MemberOtherIdentity otherId = new MemberOtherIdentity(
									SOURCE, u.getId(), u.getEmail());
							return loginOrRegistrationCompletion(otherId);
						}
						return redirect(controllers.member.routes.Login
								.loginForm("/"));
					});
		}
		Logger.debug("Code not returned from Facebook: {}", code);
		return Promise.pure(redirect(controllers.member.routes.Login
				.loginForm("/")));
	}

}
