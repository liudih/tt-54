package controllers.paypal_api;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.IDefaultSettings;
import services.member.IMemberEnquiryService;
import services.member.registration.IMemberRegistrationService;
import session.ISessionService;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.paypal_api.UserInfo;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;
import extensions.paypal_api.login.AbstractPayPalLoginProvider;

/**
 * 抽象paypal login
 * 
 * @author lijun
 *
 */

public abstract class AbstractPayPalLogin extends Controller {

	public final static String SOURCE = "paypal";
	
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
	AbstractPayPalLoginProvider provider;
	
	
	
	public Promise<Result> returnFromPayPal() {
		String code = request().getQueryString("code");
		if (!StringUtils.isEmpty(code)) {
			Promise<UserInfo> userInfo = provider
					.getAccessToken(code, provider.getRedirectUri())
					.flatMap(t -> provider.getUserInfo(t)).recover(t -> {
						Logger.error("PayPal Login Error", t);
						return null;
					});
			return userInfo
					.map(u -> {
						Logger.debug("paypal user info: {}", u);
						if (u != null) {
							MemberOtherIdentity otherId = new MemberOtherIdentity(
									SOURCE, u.getUserId(), u.getEmail());
							//代表登录或者注册成功
							boolean succeed = this.loginOrRegistration(otherId);
							if(succeed){
								return redirect(this.getSucceedRedirectUrl());
							}
						}
						return redirect(this.getFailedRedirectUrl());
					});
		}
		Logger.debug("Code not returned from PayPal: {}", code);
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
			Logger.debug("paypal用户{}在数据库中已经存在了,不需要注册直接去保存session",otherId.getEmail());
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
	 * 通过paypal登录成功后要跳转的url 子类如有需要覆盖该方法
	 * 
	 * @author lijun
	 * @return
	 */
	public abstract String getSucceedRedirectUrl();

	/**
	 * 通过paypal登录失败后要跳转的url 子类如有需要覆盖该方法
	 * 
	 * @author lijun
	 */
	public abstract String getFailedRedirectUrl();
}
