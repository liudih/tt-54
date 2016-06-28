package controllers.cart.member;

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
import utils.cart.SessionUtils;
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

/**
 * 
 * @author lijun
 *
 */
public class Login extends Controller {

	public static final Pattern emailRegx = Pattern
			.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
	
	private static final String DOMAIN = ".tomtop.com";
	private static final String HOST = "tomtop.com";
	
	
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
		String token = loginService.getToken(lf.getEmail(),
				lf.getPassword(), webCtx);

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
							.setCookie("TT_UUID", member.getCuuid(), 365 * 24 * 3600, "/",
									DOMAIN);
				} else {
					Context.current().response()
							.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/");

					Context.current().response()
							.setCookie("TT_UUID", member.getCuuid(), 365 * 24 * 3600, "/");
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
	public Result signinV0() {
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
		boolean result = loginService.authentication(lf.getEmail(),
				lf.getPassword(), webCtx);

		Map<String, Object> feedback = Maps.newHashMap();
		if (result) {
			try {
				LoginContext loginCtx = foundationService.getLoginContext();
				MemberBase member = memberService.getMemberByMemberEmail(
						lf.getEmail(), webCtx);

				if (!(loginCtx != null && loginCtx.isLogin())) {
					Logger.debug("登录过程保存session到服务端");
					// 保存session到服务端
					String sessionId = SessionUtils.getSessionID();
					if (sessionId == null) {
						return badRequest();
					}
					if (member.getCaccount() == null
							|| member.getCaccount().length() == 0) {
						MemberInSession mis = MemberInSession.newInstance(
								member.getIid(), member.getCemail()
										.toLowerCase(), member.getCemail()
										.toLowerCase(), sessionId);

						loginCtx = PlayLoginContextFactory.newLoginContext(
								member.getCemail(), member.getIgroupid(), mis);

					} else {
						MemberInSession mis = MemberInSession.newInstance(
								member.getIid(), member.getCaccount()
										.toLowerCase(), member.getCemail()
										.toLowerCase(), sessionId);
						loginCtx = PlayLoginContextFactory.newLoginContext(
								member.getCemail(), member.getIgroupid(), mis);
					}
					sessionService.set(FoundationService.LOGIN_SESSION_NAME,
							loginCtx, httpCtx);

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
	 * 注销
	 * 
	 * @return
	 */
	public Result signout() {
//		Context httpCtx = ctx();
//		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
//		sessionService.destroy(webCtx);
		
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
	 * 注册
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
		// 所有验证是否通过
		boolean allValid = true;
		// 验证邮箱是否有效
		String email = rf.getEmail();
		email = email.trim();
		email = email.toLowerCase();
		boolean emailValid = this.checkEmail(email);
		if (!emailValid) {
			allValid = false;
			feedback.put("result", "email is invalid or existed");
		}

		// 验证码
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
				// 发送激活邮件
				final String femail = email;
				Promise.promise(() -> sendValideEmail(femail));
				feedback.put("email", member.getEmail());
				feedback.put("result", "success");
				//保存登陆状态
				String sessionId = SessionUtils.getSessionID();
				MemberBase memberbase = memberService.getMemberByMemberEmail(
						member.getEmail(), webCtx);
				LoginContext loginCtx = foundationService.getLoginContext();
				if (memberbase.getCaccount() == null
						|| memberbase.getCaccount().length() == 0) {
					MemberInSession mis = MemberInSession.newInstance(
							memberbase.getIid(), memberbase.getCemail()
									.toLowerCase(), memberbase.getCemail()
									.toLowerCase(), sessionId);

					loginCtx = PlayLoginContextFactory.newLoginContext(
							memberbase.getCemail(), memberbase.getIgroupid(), mis);

				} else {
					MemberInSession mis = MemberInSession.newInstance(
							memberbase.getIid(), memberbase.getCaccount()
									.toLowerCase(), memberbase.getCemail()
									.toLowerCase(), sessionId);
					loginCtx = PlayLoginContextFactory.newLoginContext(
							memberbase.getCemail(), memberbase.getIgroupid(), mis);
				}
				sessionService.set(FoundationService.LOGIN_SESSION_NAME,
						loginCtx, httpCtx);
			}

		} else {
			//feedback.put("result", "error");
		}
		return ok(Json.toJson(feedback));
	}

	/**
	 * 验证邮箱是否已经存在
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
	 * 正则表达式验证邮箱是否正确
	 * 
	 * @author lijun
	 * @param email
	 * @return true:邮箱符合正则表达式
	 */
	private boolean regexCheckEmail(String email) {
		Matcher matcher = emailRegx.matcher(email);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 验证邮箱是否已经存在
	 * 
	 * @author lijun
	 * @param email
	 * @return true : 已经存在
	 */
	private boolean CheckEmailExisted(String email) {
		// 验证邮箱是否已经存在
		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		boolean isExist = registrationService.getEmail(email, webCtx);
		return isExist;
	}

	/**
	 * 注册的时候来验证邮箱是否合法
	 * 
	 * @param email
	 * @return true:邮箱可以注册 false:邮箱可以注册
	 */
	private boolean checkEmail(String email) {
		boolean valid = false;

		if (StringUtils.isEmpty(email)) {
			return valid;
		}
		// 首先验证邮箱是否是合法的
		boolean regexValid = this.regexCheckEmail(email);
		if (!regexValid) {
			return valid;
		}
		// 验证邮箱是否已经存在
		boolean isExist = this.CheckEmailExisted(email);
		if (!isExist) {
			valid = true;
		}
		return valid;
	}

	/**
	 * 创建验证码
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
	 * 验证验证码是否正确
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
		}else{
			resultMap.put("errorCode", EMAIL_IS_EXISTS);
		}
		return ok(Json.toJson(resultMap));
	}
	
	public Result logout() {
		loginService.logout();
		String referrer = request().getHeader("Referer");
		if(referrer!=null){
			return redirect(referrer);
		}else{
			return redirect(play.Play.application().configuration().getString("main_website"));
		}
		
	}
	
	public Map<String, Object> sendValideEmail(String email) {
		String code = UUIDGenerator.createAsString().replace("-", "");
		String mainurl = play.Play.application().configuration().getString("main_website");
		String url = mainurl + "/member/activation?activateCode="+code+"&email="+email;
		Map<String, Object> promise =null;
		// memberEmailService.verifyForWeb(email, url, code,ContextUtils.getWebContext(Context.current()));
		return promise;
	}

}
