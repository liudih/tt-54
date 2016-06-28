package controllers.google;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Http.Request;
import play.mvc.Result;
import valueobjects.google.UserInfo;
import valueobjects.member.MemberOtherIdentity;
import controllers.member.LoginProviderControllerBase;
import extensions.google.login.GoogleLoginProvider;

public class Google extends LoginProviderControllerBase {

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
	URLHelper urlHelper;

	public Promise<Result> returnFromGoogle() {
		Request request = request();
		String code = request.getQueryString(CODE);
		String state = request.getQueryString(STATE);
		if (!StringUtils.isEmpty(code)) {
			// success, get further information from Google
			Promise<UserInfo> userInfo = loginProvider
					.getAccessToken(code, urlHelper.redirectUri(), state)
					.flatMap(token -> loginProvider.getUserInfo(token))
					.recover(t -> {
						Logger.error("Google Login Error", t);
						return null;
					});
			return userInfo
					.map(u -> {
						Logger.debug("Json Result: {}", u);
						if (u != null) {
							MemberOtherIdentity otherId = new MemberOtherIdentity(
									SOURCE, u.getId(), u.getEmails().get(0)
											.getValue());
							return loginOrRegistrationCompletion(otherId);
						}
						return redirect(controllers.member.routes.Login
								.loginForm("/"));
					});
		}
		Logger.debug("Code not returned from Google: {}, state={}", code, state);
		return Promise.pure(redirect(controllers.member.routes.Login
				.loginForm("/")));
	}

}
