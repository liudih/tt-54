package controllers.member;

import interceptors.ConductRecord;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import context.WebContext;
import play.Logger;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.base.CountryService;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import services.base.FoundationService;
import services.base.captcha.CaptchaService;
import services.common.UUIDGenerator;
import services.member.IMemberEmailService;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.MemberBaseService;
import services.member.login.CryptoUtils;
import services.member.login.ILoginService;
import services.member.login.LoginServiceV2;
import services.member.registration.MemberRegistrationService;
import valueobjects.base.LoginContext;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import authenticators.member.MemberLoginAuthenticator;
import dto.Country;
import dto.member.MemberBase;
import forms.member.login.LoginForm;
import forms.member.register.RegisterForm;
import forms.member.register.RegisterUpdateForm;

public class Register extends Controller {

	@Inject
	ILoginService loginService;

	@Inject
	MemberBase mbase;

	@Inject
	IMemberEnquiryService enquiry;

	@Inject
	IMemberUpdateService mService;

	@Inject
	MemberBaseService memberBaseService;

	@Inject
	MemberRegistrationService registration;

	@Inject
	IMemberEmailService memberEmailService;

	@Inject
	CryptoUtils crypto;

	@Inject
	CaptchaService captchaService;

	@Inject
	CountryService countryEnquiryService;

	// add by lijun
	@Inject
	FoundationService fService;

	@Inject
	EventBus eventBus;

	// add by lijun
	@Inject
	LoginServiceV2 loginServiceV2;

	public final static int EMAIL_NOT_EXISTS = 0;
	public final static int EMAIL_IS_EXISTS = 1;

	public Result checkEmail(String email) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (email != null
				&& registration.getEmail(email,
						ContextUtils.getWebContext(Context.current()))) {
			resultMap.put("errorCode", EMAIL_IS_EXISTS);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", EMAIL_NOT_EXISTS);
		return ok(Json.toJson(resultMap));
	}

	public Result registerFrom() {
		play.data.Form<RegisterForm> userForm = Form.form(RegisterForm.class);
		return ok(views.html.member.register.register.render(userForm));
	}

	public Promise<Result> regValide(String email) {
		String code = UUIDGenerator.createAsString().replace("-", "");
		String url = "http://"
				+ Context.current().request().host()
				+ controllers.member.routes.Register.activate(email, code)
						.url();
		Promise<Map<String, Object>> promise = memberEmailService
				.asyncVerify(email, url, code,
						ContextUtils.getWebContext(Context.current()));
		return promise
				.map(map -> redirect(routes.Register.registerDone(email)));
	}

	@ConductRecord
	public Result register() {
		Form<RegisterForm> regForm = Form.form(RegisterForm.class)
				.bindFromRequest();
		if (regForm.hasErrors()) {
			Logger.debug("Reg Form error: {}", regForm.errorsAsJson());
			return badRequest(views.html.member.login.index.render(
					Form.form(LoginForm.class), regForm,
					loginService.getOtherLoginButtons(), null, null));
		}

		RegisterForm form = regForm.get();
		String email = form.getEmail().toLowerCase();
		MemberRegistration reg = new MemberRegistration(form.getEmail()
				.toLowerCase(), form.getPasswd(), form.getCountry(),
				form.isSignupNewsletter());
		MemberRegistrationResult result = registration.register(reg, false,
				ContextUtils.getWebContext(Context.current()));

		if (!result.isSuccess()) {
			switch (result.getType()) {
			case MEMBER_EXISTS:
				regForm.reject("email", "member.exists");
				return badRequest(views.html.member.login.index.render(
						Form.form(LoginForm.class), regForm,
						loginService.getOtherLoginButtons(), null, null));
			case MEMBER_NOT_ACTIVATED:
				return badRequest("Member exists, but not activated");
			default:
				return badRequest("Unknown error");
			}
		}

		MemberBase memberBase = new MemberBase();
		memberBase = mService.getMemberByEmail(email,
				ContextUtils.getWebContext(Context.current()));
		// modify by lijun
		loginServiceV2.forceLogin(memberBase);
		String token = loginServiceV2.getToken(memberBase);

		String uuid = memberBase.getCuuid();
		String host = Context.current().request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(loginServiceV2.HOST) != -1) {
			Context.current()
					.response()
					.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/",
							loginServiceV2.DOMAIN);

			Context.current()
					.response()
					.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/",
							loginServiceV2.DOMAIN);
		} else {
			Context.current().response()
					.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/");

			Context.current().response()
					.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/");
		}

		return redirect(routes.Register.regValide(reg.getEmail()));
	}

	public Result registerAjax() {
		Map<String, Object> mjson = new HashMap<String, Object>();
		Form<RegisterForm> regForm = Form.form(RegisterForm.class)
				.bindFromRequest();
		if (regForm.hasErrors()) {
			mjson.put("result", "regwrong");
			return ok(Json.toJson(mjson));
		}
		RegisterForm form = regForm.get();
		MemberRegistration reg = new MemberRegistration(form.getEmail(),
				form.getPasswd(), form.getCountry(), form.isSignupNewsletter());
		MemberRegistrationResult result = registration.register(reg, false,
				ContextUtils.getWebContext(Context.current()));
		if (!result.isSuccess()) {
			switch (result.getType()) {
			case MEMBER_EXISTS:
				mjson.put("result", "exists");
				return ok(Json.toJson(mjson));
			case MEMBER_NOT_ACTIVATED:
				mjson.put("result", "exists-but-not-activated");
				return ok(Json.toJson(mjson));
			default:
				mjson.put("result", "unknownerror");
				return ok(Json.toJson(mjson));
			}
		}
		MemberBase memberBase = new MemberBase();
		memberBase = mService.getMemberByEmail(reg.getEmail(),
				ContextUtils.getWebContext(Context.current()));
		loginServiceV2.forceLogin(memberBase);

		String token = loginServiceV2.getToken(memberBase);

		String uuid = memberBase.getCuuid();
		String host = Context.current().request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(HOST) != -1) {
			Context.current()
					.response()
					.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/",
							loginServiceV2.DOMAIN);

			Context.current()
					.response()
					.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/",
							loginServiceV2.DOMAIN);
		} else {
			Context.current().response()
					.setCookie("TT_TOKEN", token, 365 * 24 * 3600, "/");

			Context.current().response()
					.setCookie("TT_UUID", uuid, 365 * 24 * 3600, "/");
		}

		mjson.put("sendmail", routes.Register.registerDone(reg.getEmail())
				.toString());
		mjson.put("result", "success");
		return ok(Json.toJson(mjson));
	}

	public Result registerDone(String email) {
		return ok(views.html.member.register.done.render(email));
	}

	public Result followupQuestions(String email) {
		return redirect(routes.Register.registerUpdateFrom());
	}

	public Result notActivatedYet(String email) {
		return ok(views.html.member.register.not_activated.render(email));
	}

	public Result hasBeenActivated() {
		return ok(views.html.member.register.hasBeen_activated.render());
	}

	public Result hasBeenSent() {
		return ok(views.html.member.register.hasBeen_sent.render());
	}

	public Promise<Result> activateSuccess(String email) {
		Promise<Map<String, Object>> promise = registration
				.asyncRegSuccess(email);
		return promise
				.map(map -> redirect(routes.Register.activateSuccessful()));
	}

	public Result activate(String email, String code) {
		int result = memberEmailService.activation(code,
				ContextUtils.getWebContext(Context.current()));
		if (result == 1 && email != null) {

			return redirect(routes.Register.activateSuccess(email));
		}
		if (result == -2 && email != null) {
			return redirect(routes.Register.hasBeenActivated());
		}
		return redirect(routes.Register.notActivatedYet(email));
	}

	public Result activateSuccessful() {
		return ok(views.html.member.register.activated.render());
	}

	public Result doResendActivationEmail() {
		MemberInSession session = loginService.getLoginData();
		String email = session.getEmail();
		if (StringUtils.isEmpty(email)) {
			return badRequest("no member!");
		}
		MemberBase user = mService.getMemberByEmail(email,
				ContextUtils.getWebContext(Context.current()));
		if (null != user && user.isBactivated()) {
			return redirect(routes.Register.hasBeenActivated());
		}

		// 重新发送邮件次数限制
		if (!registration.resendActivationEmail(email)) {
			Logger.error("Activation email has been sent: {}", email);
			return redirect(routes.Register.hasBeenSent());
		}
		return redirect(routes.Register.regValide(email));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Result registerUpdateFrom() {
		MemberInSession session = loginService.getLoginData();
		String email = session.getEmail();
		RegisterUpdateForm rr = new RegisterUpdateForm();
		mbase = mService.getMemberByEmail(email,
				ContextUtils.getWebContext(Context.current()));
		if (mbase != null && mbase.getDbirth() != null) {
			Date birth = mbase.getDbirth();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(birth);
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			rr.setDay(day);
			rr.setMonth(month);
			rr.setYear(year);
		}
		if (mbase.getCcountry() != null) {
			Country country = countryEnquiryService
					.getCountryByShortCountryName(mbase.getCcountry());
			if (country != null) {
				rr.setCountryName(country.getCname());
			}

		}
		BeanUtils.copyProperties(mbase, rr);
		Form<RegisterUpdateForm> userupdateForm = Form.form(
				RegisterUpdateForm.class).fill(rr);
		return ok(views.html.member.register.registerupdate
				.render(userupdateForm));
	}

	public Result addRegister() throws ParseException {
		Form<RegisterUpdateForm> userupdateForm = Form.form(
				RegisterUpdateForm.class).bindFromRequest();
		if (userupdateForm.hasErrors()) {
			return badRequest();
		}
		RegisterUpdateForm form = userupdateForm.get();
		// 获取验证码
		String[] arraycaptchas = request().body().asFormUrlEncoded()
				.get("captcha");
		if (arraycaptchas == null || (!(arraycaptchas.length > 0))) {
			return badRequest();
		}
		String captcha = arraycaptchas[0];
		// 由于RegisterUpdateForm中的一些字段没有做必填验证,这里对一些字段单独验证
		String oldPwd = form.getCpassword();
		String email = loginService.getLoginEmail();
		if (StringUtils.isEmpty(email)
				|| StringUtils.isEmpty(form.getCnewpassword())
				|| StringUtils.isEmpty(form.getCcnewpassword())
				|| StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(captcha)) {
			return badRequest();
		}
		// 验证码验证结果
		boolean flag = captchaService.verify(captcha);
		if (!flag) {
			return badRequest();
		}
		// 数据库中的用户密码和前台用户提交的旧密码做校验
		WebContext webContext = fService.getWebContext();
		boolean oldPwdCorrect = loginService.authentication(email, oldPwd,
				webContext);
		if (!oldPwdCorrect) {
			return badRequest();
		}

		if (mService.savaMember(form,
				ContextUtils.getWebContext(Context.current()))) {
			if (form.getCnewpassword() != null) {
				loginService.logout();
				return redirect(controllers.member.routes.Login.loginForm("/"));
			} else {
				return redirect(controllers.member.routes.Register
						.registerUpdateFrom());
			}
		}
		return badRequest();
	}

	public Result profileUpdate() {
		Form<RegisterUpdateForm> userupdateForm = Form.form(
				RegisterUpdateForm.class).bindFromRequest();
		if (userupdateForm.hasErrors()) {
			return badRequest();
		}
		RegisterUpdateForm form = userupdateForm.get();
		if (form.getMonth() != null && form.getDay() != null
				& form.getYear() != null) {
			String month = Integer.valueOf(form.getMonth()) < 10 ? "0"
					+ form.getMonth() : form.getMonth();
			String day = Integer.valueOf(form.getDay()) < 10 ? "0"
					+ form.getDay() : form.getDay();

			String date = form.getYear() + "-" + month + "-" + day;
			Logger.debug("tocken:{}", date);
			form.setDbirth(DateFormatUtils.getFormatDateByStr(date));
		}
		WebContext webContext = fService.getWebContext();
		LoginContext loginCtx = fService.getLoginContext(webContext);
		String email = loginCtx.getMemberID();
		// 如果在当前上下文找不到登录的用户直接更新失败
		if (StringUtils.isEmpty(email)) {
			return badRequest();
		}
		MemberBase mmbase = mService.getMemberByEmail(email, webContext);
		BeanUtils.copyProperties(form, mmbase);
		mmbase.setCemail(email);
		mService.updateMember(mmbase);
		return redirect(controllers.member.routes.Register.registerUpdateFrom());
	}

	public Result translatePassword() {
		Form<RegisterUpdateForm> userupdateForm = Form.form(
				RegisterUpdateForm.class).bindFromRequest();
		RegisterUpdateForm registerUpdateForm = userupdateForm.get();
		MemberInSession session = loginService.getLoginData();
		String email = session.getEmail();
		mbase = mService.getMemberByEmail(email,
				ContextUtils.getWebContext(Context.current()));
		boolean result;
		String passwords = registerUpdateForm.getCpassword();
		result = crypto.validateHash(passwords, mbase.getCpasswd());
		return ok(Json.toJson(result));
	}

}