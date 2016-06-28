package controllers.base.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.Logger;
import play.data.Form;
import play.libs.F.Function;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICaptchaService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.common.UUIDGenerator;
import services.member.IMemberEmailService;
import services.member.IMemberEnquiryService;
import services.member.login.ILoginService;
import services.member.registration.IMemberRegistrationService;
import session.ISessionService;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.primitives.Bytes;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;
import events.member.LoginEvent;
import forms.member.login.LoginForm;
import forms.member.register.RegisterForm;
import utils.base.SessionUtils;

/**
 * 
 * @author lijun
 *
 */
public class Login extends Controller {

	public static final Pattern emailRegx = Pattern
			.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	@Inject
	ILoginService loginService;

	@Inject
	IMemberEnquiryService memberService;

	@Inject
	FoundationService foundationService;

	@Inject
	ISessionService sessionService;

	@Inject
	EventBus eventBus;

	@Inject
	IMemberRegistrationService registrationService;

	@Inject
	IMemberEmailService memberEmailService;

	@Inject
	ICaptchaService captchaService;
	public final static int EMAIL_NOT_EXISTS = 0;
	public final static int EMAIL_IS_EXISTS = 1;

	private static final String DOMAIN = ".tomtop.com";
	private static final String HOST = "tomtop.com";

	public Result signin() {
		Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();

		Map<String, String> returnjson = new HashMap<String, String>();
		if (form.hasErrors()) {
			returnjson.put("result", "error");
			return ok(Json.toJson(returnjson));
		}

		LoginForm lf = form.get();
		lf.setEmail(lf.getEmail().toLowerCase());
		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		// 验证密码是否正确
		String token = loginService.getToken(lf.getEmail(), lf.getPassword(),
				webCtx);

		Map<String, Object> feedback = Maps.newHashMap();
		if (token != null && token.length() > 0) {
			try {
				LoginContext loginCtx = foundationService.getLoginContext();
				MemberBase member = memberService.getMemberByMemberEmail(
						lf.getEmail(), webCtx);

				String host = Context.current().request().getHeader("Host");
				if (host != null && host.indexOf(HOST) != -1) {
					Context.current()
							.response()
							.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/",
									DOMAIN);

					Context.current()
							.response()
							.setCookie("TT_UUID", member.getCuuid(),
									365 * 24 * 3600, "/", DOMAIN);
				} else {
					Context.current().response()
							.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/");

					Context.current()
							.response()
							.setCookie("TT_UUID", member.getCuuid(),
									365 * 24 * 3600, "/");
				}

				String ip = foundationService.getClientIP();
				int siteID = foundationService.getSiteID();
				LoginEvent loginEvent = new LoginEvent(loginCtx.getLTC(),
						loginCtx.getSTC(), ip, siteID, member.getCemail()
								.toLowerCase(), FoundationService.DEVICE_NAME);
				eventBus.post(loginEvent);
				loginService.executeLoginProcess(loginEvent);
				feedback.put("result", "success");
			} catch (Exception e) {
				Logger.error("save session failed:{}", lf.getEmail(), e);
				feedback.put("result", "someting is wrong!");
				return ok(Json.toJson(feedback));
			}
		} else {
			feedback.put("result", "password is wrong!");
			return ok(Json.toJson(feedback));
		}
		return ok(Json.toJson(feedback));
	}

	/**
	 * 娉ㄩ攢
	 * 
	 * @return
	 */
	public Result signout() {
		String host = Context.current().request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(HOST) != -1) {
			Context.current().response().discardCookie("TT_TOKEN", "/", DOMAIN);

			Context.current().response().discardCookie("TT_UUID", "/", DOMAIN);
		} else {
			Context.current().response().discardCookie("TT_TOKEN", "/");

			Context.current().response().discardCookie("TT_UUID", "/");
		}

		return redirect("/");
	}

	/**
	 * 娉ㄥ唽
	 * 
	 * @return
	 */
	public Result register() {
		Form<RegisterForm> form = Form.form(RegisterForm.class)
				.bindFromRequest(request());
		if (form.hasErrors()) {
			return badRequest();
		}
		HashMap<String, Object> feedback = Maps.newHashMap();
		RegisterForm rf = form.get();
		// 鎵�鏈夐獙璇佹槸鍚﹂�氳繃
		boolean allValid = true;
		// 楠岃瘉閭鏄惁鏈夋晥
		String email = rf.getEmail();
		email = email.trim();
		email = email.toLowerCase();
		boolean emailValid = this.checkEmail(email);
		if (!emailValid) {
			allValid = false;
			feedback.put("result", "email is invalid or existed");
		}

		// 楠岃瘉鐮�
		String captcha = rf.getCode();

		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		boolean codeValid = captchaService.verify(captcha, webCtx);
		if (!codeValid) {
			allValid = false;
			feedback.put("result", "captcha is error");
		}
		if (allValid) {
			MemberRegistration member = new MemberRegistration(rf.getEmail()
					.toLowerCase(), rf.getPasswd(), rf.getCountry(), false);
			MemberRegistrationResult mrResult = registrationService.register(
					member, false, webCtx);
			if (!mrResult.isSuccess()) {
				feedback.put("result", "error");
				switch (mrResult.getType()) {
				case MEMBER_EXISTS:
					feedback.put("result", "email is invalid or existed");
				case MEMBER_NOT_ACTIVATED:
					feedback.put("result", "member exists, but not activated");
				default:
					feedback.put("result", "unknown error");
				}
			} else {
				// 鍙戦�佹縺娲婚偖浠�
				final String femail = email;
				Promise.promise(() -> sendValideEmail(femail));
				feedback.put("email", member.getEmail());
				feedback.put("result", "success");

				// 验证密码是否正确
				String token = loginService.getToken(email, rf.getPasswd(),
						webCtx);

				if (token != null && token.length() > 0) {
					try {
						MemberBase memberBase = memberService
								.getMemberByMemberEmail(email, webCtx);

						String host = Context.current().request()
								.getHeader("Host");
						if (host != null && host.indexOf(HOST) != -1) {
							Context.current()
									.response()
									.setCookie("TT_TOKEN", token,
											365 * 24 * 3600, "/", DOMAIN);

							Context.current()
									.response()
									.setCookie("TT_UUID",
											memberBase.getCuuid(),
											365 * 24 * 3600, "/", DOMAIN);
						} else {
							Context.current()
									.response()
									.setCookie("TT_TOKEN", token,
											365 * 24 * 3600, "/");

							Context.current()
									.response()
									.setCookie("TT_UUID",
											memberBase.getCuuid(),
											365 * 24 * 3600, "/");
						}
					} catch (Exception e) {
						Logger.error("save session failed:{}", email, e);
						feedback.put("result", "someting is wrong!");
						return ok(Json.toJson(feedback));
					}
				}

			}

		} else {
			// feedback.put("result", "error");
		}
		return ok(Json.toJson(feedback));
	}

	/**
	 * 楠岃瘉閭鏄惁宸茬粡瀛樺湪
	 * 
	 * @param email
	 * @return
	 */
	public Result validateEmail(String email) {
		boolean isValid = this.checkEmail(email);
		if (!isValid) {
			Logger.debug("email:{} invalid", email);
			return badRequest();
		}
		HashMap<String, Object> result = Maps.newHashMap();
		result.put("succeed", true);
		Logger.debug("email:{} valid", email);
		return ok(Json.toJson(result));
	}

	/**
	 * 姝ｅ垯琛ㄨ揪寮忛獙璇侀偖绠辨槸鍚︽纭�
	 * 
	 * @author lijun
	 * @param email
	 * @return true:閭绗﹀悎姝ｅ垯琛ㄨ揪寮�
	 */
	private boolean regexCheckEmail(String email) {
		Matcher matcher = emailRegx.matcher(email);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 楠岃瘉閭鏄惁宸茬粡瀛樺湪
	 * 
	 * @author lijun
	 * @param email
	 * @return true : 宸茬粡瀛樺湪
	 */
	private boolean CheckEmailExisted(String email) {
		// 楠岃瘉閭鏄惁宸茬粡瀛樺湪
		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		boolean isExist = registrationService.getEmail(email, webCtx);
		return isExist;
	}

	/**
	 * 娉ㄥ唽鐨勬椂鍊欐潵楠岃瘉閭鏄惁鍚堟硶
	 * 
	 * @param email
	 * @return true:閭鍙互娉ㄥ唽 false:閭鍙互娉ㄥ唽
	 */
	private boolean checkEmail(String email) {
		boolean valid = false;

		if (StringUtils.isEmpty(email)) {
			return valid;
		}
		// 棣栧厛楠岃瘉閭鏄惁鏄悎娉曠殑
		boolean regexValid = this.regexCheckEmail(email);
		if (!regexValid) {
			return valid;
		}
		// 楠岃瘉閭鏄惁宸茬粡瀛樺湪
		boolean isExist = this.CheckEmailExisted(email);
		if (!isExist) {
			valid = true;
		}
		return valid;
	}

	/**
	 * 鍒涘缓楠岃瘉鐮�
	 * 
	 * @return
	 * @throws IOException
	 */
	public Result createCaptcha() throws IOException {
		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		List<Byte> captcha = captchaService.createCaptcha(webCtx);
		byte[] bytes = Bytes.toArray(captcha);
		response().setHeader("Cache-Control", "no-store, no-cache");
		return ok(bytes).as("image/png");
	}

	/**
	 * 楠岃瘉楠岃瘉鐮佹槸鍚︽纭�
	 * 
	 * @return
	 */
	public Result validateCategory() {
		if (request().body().asFormUrlEncoded().get("captcha") == null) {
			return ok(Json.toJson(false));
		}
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		if (captcha == null || captcha.length() == 0) {
			return ok(Json.toJson(false));
		} else {
			Context httpCtx = ctx();
			WebContext webCtx = ContextUtils.getWebContext(httpCtx);
			boolean result = captchaService.verify(captcha, webCtx);
			return ok(Json.toJson(result));
		}
	}

	public Result validEmail(String email) {
		boolean flag = this.checkEmail(email);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (flag) {
			resultMap.put("errorCode", EMAIL_NOT_EXISTS);
		} else {
			resultMap.put("errorCode", EMAIL_IS_EXISTS);
		}
		return ok(Json.toJson(resultMap));
	}

	public Result logout() {
		String host = Context.current().request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(HOST) != -1) {
			Context.current().response().discardCookie("TT_TOKEN", "/", DOMAIN);

			Context.current().response().discardCookie("TT_UUID", "/", DOMAIN);
		} else {
			Context.current().response().discardCookie("TT_TOKEN", "/");

			Context.current().response().discardCookie("TT_UUID", "/");
		}
		String referrer = request().getHeader("Referer");
		if (referrer != null) {
			return redirect(referrer);
		} else {
			return redirect(play.Play.application().configuration()
					.getString("main_website"));
		}

	}

	public Map<String, Object> sendValideEmail(String email) {
		String code = UUIDGenerator.createAsString().replace("-", "");
		String mainurl = play.Play.application().configuration()
				.getString("main_website");
		String url = mainurl + "/member/activation?activateCode=" + code
				+ "&email=" + email;
		Map<String, Object> promise = null;
		// memberEmailService.verifyForWeb(email, url,
		// code,ContextUtils.getWebContext(Context.current()));
		return promise;
	}
	
	public Result isLogin(){
		Map<String, String> isLogin = new HashMap<String, String>();
		LoginContext lc = foundationService.getLoginContext();
		if(lc.isLogin()){
			isLogin.put("isLongin", "true");
		}else{
			isLogin.put("isLongin", "false");
		}
		return ok(Json.toJson(isLogin));
	}

}
