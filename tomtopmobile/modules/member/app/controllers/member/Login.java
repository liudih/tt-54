package controllers.member;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.Logger;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICaptchaService;
import services.ICountryService;
import services.ISystemParameterService;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import services.member.IMemberEmailService;
import services.member.IMemberEnquiryService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import services.member.login.ILoginService;
import services.member.registration.IMemberRegistrationService;
import session.ISessionService;
import utils.SessionUtils;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.primitives.Bytes;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.Country;
import dto.member.MemberBase;
import events.member.LoginEvent;
import extensions.member.login.ILoginProvider;
import forms.member.findPasswd.FindPasswordForm;
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
	ICaptchaService captchaService;

	@Inject
	IFindPasswordService findPasswordService;

	@Inject
	ISystemParameterService systemParameterService;

	@Inject
	IForgetPasswdBaseService forgetPasswdBaseService;

	@Inject
	IMemberEmailService memberEmailService;

	@Inject
	Set<ILoginProvider> loginProvider;

	@Inject
	ICountryService countryService;

	/**
	 * 登录 注册 视图 入口
	 * 
	 * @param type
	 *            0 登录 1注册
	 * @return
	 */
	public Result login(int type) {
		if (request().getQueryString("backUrl") != null) {
			String callback = request().getQueryString("backUrl");
			response().setCookie("callback", callback);
		} else if (request().hasHeader("Referer")) {
			String callback = request().getHeader("Referer");
			response().setCookie("callback", callback);
		}
		List<Country> countrys = foundationService.getAllCountries();
		// 获取国家
		Country country = foundationService.getCountryObj();
		// 排序
		List<ILoginProvider> loginProviders = FluentIterable
				.from(loginProvider).toSortedList(
						new Comparator<ILoginProvider>() {
							@Override
							public int compare(ILoginProvider p1,
									ILoginProvider p2) {
								if (p1.getDisplayOrder() > p2.getDisplayOrder()) {
									return 1;
								} else if (p1.getDisplayOrder() < p2
										.getDisplayOrder()) {
									return -1;
								} else {
									return 0;
								}
							}
						});
		return ok(views.html.member.login.render(country, countrys, type,
				loginProviders));
	}

	public Result signin() {
		Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest();
		}

		LoginForm lf = form.get();
		lf.setEmail(lf.getEmail().toLowerCase());
		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		// 验证密码是否正确
		boolean result = loginService.authentication(lf.getEmail(),
				lf.getPassword(), webCtx);

		// 验证密码是否正确
		String token = loginService.getToken(lf.getEmail(), lf.getPassword(),
				webCtx);

		Map<String, Object> feedback = Maps.newHashMap();
		if (token != null && token.length() > 0) {
			try {
				LoginContext loginCtx = foundationService.getLoginContext();
				MemberBase member = memberService.getMemberByMemberEmail(
						lf.getEmail(), webCtx);

				// if (!(loginCtx != null && loginCtx.isLogin())) {
				// Logger.debug("登录过程保存session到服务端");
				// // 保存session到服务端
				// String sessionId = SessionUtils.getSessionID();
				// if (sessionId == null) {
				// return badRequest();
				// }
				// if (member.getCaccount() == null
				// || member.getCaccount().length() == 0) {
				// MemberInSession mis = MemberInSession.newInstance(
				// member.getIid(), member.getCemail()
				// .toLowerCase(), member.getCemail()
				// .toLowerCase(), sessionId);
				//
				// loginCtx = PlayLoginContextFactory.newLoginContext(
				// member.getCemail(), member.getIgroupid(), mis);
				//
				// } else {
				// MemberInSession mis = MemberInSession.newInstance(
				// member.getIid(), member.getCaccount()
				// .toLowerCase(), member.getCemail()
				// .toLowerCase(), sessionId);
				// loginCtx = PlayLoginContextFactory.newLoginContext(
				// member.getCemail(), member.getIgroupid(), mis);
				// }
				// sessionService.set(FoundationService.LOGIN_SESSION_NAME,
				// loginCtx, httpCtx);
				//
				// }

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
				feedback.put("succeed", true);
			} catch (Exception e) {
				Logger.error("save session failed:{}", lf.getEmail(), e);
				return badRequest();
			}
		} else {
			return badRequest();
		}
		return ok(Json.toJson(feedback));
	}

	/**
	 * 注销
	 * 
	 * @return
	 */
	public Result signout() {
		// Context httpCtx = ctx();
		// WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		// sessionService.destroy(webCtx);
		// return redirect("/");
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
			feedback.put("email", "email is invalid or existed");
		}

		// 验证码
		String captcha = rf.getCode();

		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		boolean codeValid = captchaService.verify(captcha, webCtx);
		if (!codeValid) {
			allValid = false;
			feedback.put("code", "captcha is error");
		}
		if (allValid) {
			MemberRegistration member = new MemberRegistration(rf.getEmail()
					.toLowerCase(), rf.getPasswd(), rf.getCountry(), false);
			MemberRegistrationResult mrResult = registrationService.register(
					member, false, webCtx);
			if (!mrResult.isSuccess()) {
				feedback.put("succeed", false);
				switch (mrResult.getType()) {
				case MEMBER_EXISTS:
					feedback.put("email", "email is invalid or existed");
				case MEMBER_NOT_ACTIVATED:
					feedback.put("email", "member exists, but not activated");
				default:
					feedback.put("email", "unknown error");
				}
			} else {
				// 发送激活邮件
				this.sendValideEmail(email);
				feedback.put("succeed", true);

				String token = loginService.getToken(rf.getEmail()
						.toLowerCase(), rf.getPasswd(), webCtx);
				// 注册后登陆，验证密码是否正确
				MemberBase memberbase = memberService.getMemberByMemberEmail(
						rf.getEmail(), webCtx);
				if (token != null && token.length() > 0) {
					String host = Context.current().request().getHeader("Host");
					if (host != null && host.indexOf(HOST) != -1) {
						Context.current()
								.response()
								.setCookie("TT_TOKEN", token, 365 * 24 * 3600,
										"/", DOMAIN);

						Context.current()
								.response()
								.setCookie("TT_UUID", memberbase.getCuuid(),
										365 * 24 * 3600, "/", DOMAIN);
					} else {
						Context.current()
								.response()
								.setCookie("TT_TOKEN", token, 365 * 24 * 3600,
										"/");

						Context.current()
								.response()
								.setCookie("TT_UUID", memberbase.getCuuid(),
										365 * 24 * 3600, "/");
					}
				}
			}

		} else {
			feedback.put("succeed", false);
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
	public Result validateCategory(String captcha) {
		Map<String, Object> feedback = Maps.newHashMap();
		if (captcha == null || captcha.length() == 0) {
			feedback.put("succeed", false);
		} else {
			Context httpCtx = ctx();
			WebContext webCtx = ContextUtils.getWebContext(httpCtx);
			boolean result = captchaService.verify(captcha, webCtx);
			feedback.put("succeed", result);
		}
		return ok(Json.toJson(feedback));
	}

	/**
	 * 忘记密码视图
	 * 
	 * @return
	 */
	public Result forgetPasswordView() {
		// 注销当前用户
		loginService.logout();
		return ok(views.html.member.forgetPassword.render());
	}

	/**
	 * 忘记密码
	 * 
	 * @return
	 */
	public Result forgetPassword() {
		Form<FindPasswordForm> form = Form.form(FindPasswordForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}
		FindPasswordForm fForm = form.get();
		String email = fForm.getEmail();
		boolean isExis = false;
		if (!StringUtils.isEmpty(email)) {
			isExis = (regexCheckEmail(email) && CheckEmailExisted(email)) ? true
					: false;
		}
		if (!isExis) {
			return redirect(routes.Login.checkEmail());
		} else {
			if (!validateTimeout(email)) {
				return redirect(routes.Login.timeout());
			} else {
				forgetPasswordSendEmail(email);
			}
		}
		return redirect(routes.Login.findPasswordResult());
	}

	/**
	 * 链接超时验证
	 */
	private boolean validateTimeout(String email) {
		boolean result = false;
		int siteId = foundationService.getSiteID();
		String dayNums = systemParameterService.getSystemParameter(siteId,
				foundationService.getLanguage(), "FindPassValideNums");
		int dayNum = Integer.valueOf(dayNums).intValue();
		List<Date> dateList = DateFormatUtils.getNowDayRange(0);
		int nums = forgetPasswdBaseService.getCount(email, dateList.get(0),
				dateList.get(1), ContextUtils.getWebContext(Context.current()));
		if (nums < dayNum) {
			result = true;
		}
		return result;

	}

	/**
	 * 忘记密码发送邮件
	 */
	public void forgetPasswordSendEmail(String email) {
		String url = "http://"
				+ Context.current().request().host()
				+ controllers.member.routes.ResetPassword
						.resetPasswordValide("[cid]");
		boolean promise = findPasswordService.asyncFindPassword(email, url,
				ContextUtils.getWebContext(Context.current()));
		if (promise) {
			Logger.info("mobile invoke send email success!email=" + email);
		} else {
			Logger.error("mobile invoke send email error!email=" + email);
		}

	}

	/**
	 * 验证邮箱是否已经存在
	 * 
	 * @param email
	 * @return
	 */
	public Result validateEmailIsExist(String email) {
		boolean isValid = (regexCheckEmail(email) && CheckEmailExisted(email));
		if (!isValid) {
			Logger.debug("email:{} invalid", email);
			return badRequest();
		}
		HashMap<String, Object> result = Maps.newHashMap();
		result.put("succeed", true);
		Logger.debug("email:{} valid", email);
		return ok(Json.toJson(result));
	}

	public Result findPasswordResult() {
		String result = "Validation email has been sent to your mailbox";
		return ok(views.html.base.public_information_show.render(result));
	}

	public Result checkEmail() {
		String result = "please check your email";
		return ok(views.html.base.public_information_show.render(result));
	}

	public Result timeout() {
		String result = "Retrieve password more than three times,retrieve password link failed! Can only find three times a day!";
		return ok(views.html.base.public_information_show.render(result));
	}

	/**
	 * 新用户注册后发送验证邮件到邮箱
	 * 
	 * @author lijun
	 * @param email
	 * @return
	 * @return
	 * @return
	 */
	private Promise<Boolean> sendValideEmail(String email) {
		return Promise.promise(new Function0<Boolean>() {
			@Override
			public Boolean apply() {
				String url = "http://"
						+ Context.current().request().host()
						+ controllers.member.routes.Login.activeMember(null,
								null).url();
				Logger.debug("{}", url);
				boolean succeed = memberEmailService
						.sendValideEmail(email, url);
				return succeed;
			}
		});

	}

	/*
	 * 账户激活认证
	 * 
	 * @param email
	 * 
	 * @param code
	 * 
	 * @return
	 */
	public Result activeMember(String email, String code) {
		String message = "";
		if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(code)) {
			int result = memberEmailService.activation(code,
					ContextUtils.getWebContext(Context.current()));
			Logger.debug("result:{}", result);
			if (result == 1) {
				// 成功
				message = "your account is successfully activated!";
				return ok(views.html.base.public_information_show
						.render(message));
			} else if (result == -2) {
				// 已激活
				message = "Your account has already been activated!";
				return ok(views.html.base.public_information_show
						.render(message));
			}
			// 失败
			message = "Activation Failed!";
			return ok(views.html.base.public_information_show.render(message));
		}
		message = "Activation Failed!";
		return ok(views.html.base.public_information_show.render(message));
	}

	public Result registerSuccess() {
		String email = request().getQueryString("email");
		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		LoginContext loginCtx = foundationService.getLoginContext();
		MemberBase member = memberService.getMemberByMemberEmail(email, webCtx);

		if (null != email && this.CheckEmailExisted(email)) {
			return ok(views.html.member.register_success.render(email));
		}

		return redirect("/");
	}

	/**
	 * 验证国家是否存在
	 * 
	 * @param country
	 * @return
	 */
	public Result validateCountry(Integer countryId, String country) {
		HashMap<String, Object> result = Maps.newHashMap();
		if (countryId == null || country == null) {
			result.put("error", "country is null");
			return ok(Json.toJson(result));
		}
		Country ct = countryService.getCountryByCountryId(countryId);
		if (ct == null) {
			result.put("error", "country not find");
			return ok(Json.toJson(result));
		}
		String name = ct.getCname();
		if (!country.equals(name)) {
			result.put("error", "country is invalid");
			return ok(Json.toJson(result));
		}
		result.put("succeed", true);
		Logger.debug("country:{} valid", countryId);
		return ok(Json.toJson(result));
	}
}
