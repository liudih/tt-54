package controllers.google_api;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import services.IDefaultSettings;
import services.member.IMemberEnquiryService;
import services.member.registration.IMemberRegistrationService;
import session.ISessionService;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.google.UserInfo;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;
import extensions.google_api.login.AbstractGoogleLoginProvider;

/**
 * 抽象FacebookController
 * 
 * @author lijun
 *
 */

public abstract class AbstractGoogle extends Controller {

	public static final String SOURCE = "google";

	public static final String ERROR = "error";
	public static final String ERROR_REASON = "error_reason";
	public static final String ERROR_DESCRIPTION = "error_description";

	public static final String TOKEN = "access_token";
	public static final String CODE = "code";
	public static final String STATE = "state";
	public static final String GRANTED_SCOPE = "granted_scopes";
	
	final static String LOGIN_SESSION_NAME = "LOGIN_CONTEXT";


	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	ISessionService sessionService;

	@Inject
	IDefaultSettings defaultSettings;

	@Inject
	IMemberRegistrationService registrationService;

	@Inject
	AbstractGoogleLoginProvider provider;
	
	
	
	public Promise<Result> returnFromGoogle() {
		Request request = request();
		String code = request.getQueryString(CODE);
		String state = request.getQueryString(STATE);
		if (!StringUtils.isEmpty(code)) {
			// success, get further information from Google
			Promise<UserInfo> userInfo = provider
					.getAccessToken(code, provider.getRedirectUri(), state)
					.flatMap(token -> provider.getUserInfo(token))
					.recover(t -> {
						Logger.error("Google Login Error", t);
						return null;
					});
			return userInfo
					.map(u -> {
						Logger.debug("google Json Result: {}", u);
						if (u != null) {
							MemberOtherIdentity otherId = new MemberOtherIdentity(
									SOURCE, u.getId(), u.getEmails().get(0)
											.getValue());
							//代表登录或者注册成功
							boolean succeed = this.loginOrRegistration(otherId);
							if(succeed){
								return redirect(this.getSucceedRedirectUrl());
							}
						}
						return redirect(this.getFailedRedirectUrl());
					});
		}
		Logger.debug("Code not returned from Google: {}, state={}", code, state);
		return Promise.pure(redirect(this.getFailedRedirectUrl()));
	}

	/**
	 * 登录或者注册用户
	 * 
	 * @return
	 */
	private boolean loginOrRegistration(MemberOtherIdentity otherId) {
		if (otherId == null) {
			return false;
		}
		if (otherId.getEmail() == null || otherId.getEmail().length() == 0) {
			//flash("login_message", "Email Authorization is Required");
			return false;
		}
		WebContext webCtx = ContextUtils.getWebContext(Context.current());
		MemberBase mb = memberEnquiryService.getMemberByOtherIdentity(otherId,
				webCtx);

		if (mb != null) {
			Logger.debug("facebook用户{}在数据库中已经存在了,不需要注册直接去保存session",otherId.getEmail());
			// 保存当前用户的session
			this.saveSession(mb);
		} else {
			// registration
			String countryCode = defaultSettings.getCountryCode(webCtx);
			MemberRegistration reg = new MemberRegistration(otherId.getEmail(),
					null, countryCode, true);
			MemberRegistrationResult result = registrationService.register(reg,
					otherId, webCtx);
			if (!result.isSuccess()) {
				return false;
			}
			mb = memberEnquiryService.getMemberByOtherIdentity(otherId, webCtx);
			// 保存当前用户的session
			this.saveSession(mb);
		}

		return true;
	}

	/**
	 * 保存当前用户的session
	 * 
	 * @author lijun
	 * @param member
	 */
	private void saveSession(MemberBase member) {
		WebContext webCtx = ContextUtils.getWebContext(Context.current());
		String sessionId = webCtx.getLtc();
		if (member.getCaccount() == null || member.getCaccount().length() == 0) {
			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					member.getCemail().toLowerCase(), member.getCemail()
							.toLowerCase(), sessionId);
			LoginContext loginCtx = PlayLoginContextFactory.newLoginContext(
					member.getCemail(), member.getIgroupid(), mis);

			sessionService.set(LOGIN_SESSION_NAME, loginCtx, webCtx);

		} else {
			MemberInSession mis = MemberInSession.newInstance(member.getIid(),
					member.getCaccount().toLowerCase(), member.getCemail()
							.toLowerCase(), sessionId);
			LoginContext loginCtx = PlayLoginContextFactory.newLoginContext(
					member.getCemail(), member.getIgroupid(), mis);
			sessionService.set(LOGIN_SESSION_NAME, loginCtx, webCtx);
		}
	}

	/**
	 *
	 * 通过Facebook登录成功后要跳转的url 子类如有需要覆盖该方法
	 * 
	 * @author lijun
	 * @return
	 */
	public abstract String getSucceedRedirectUrl();

	/**
	 * 通过Facebook登录失败后要跳转的url 子类如有需要覆盖该方法
	 * 
	 * @author lijun
	 */
	public abstract String getFailedRedirectUrl();
}
