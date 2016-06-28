package controllers.member;

import interceptors.ConductRecord;

import java.util.HashMap;
import java.util.Map;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.dodocool.base.FoundationService;
import services.dodocool.member.MemberLoginService;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.login.ILoginService;
import services.member.registration.IMemberRegistrationService;
import services.util.CryptoUtils;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;

import com.google.inject.Inject;

import context.ContextUtils;
import dto.Country;
import dto.member.MemberBase;
import forms.member.register.RegisterForm;
import forms.member.register.RegisterUpdateForm;

public class Register extends Controller {
	@Inject
	IMemberRegistrationService registration;

	@Inject
	ILoginService loginService;

	@Inject
	IMemberEnquiryService mService;
	
	@Inject
	IMemberUpdateService iMemberUpdateService;

	@Inject
	FoundationService foundationService;

	@Inject
	MemberLoginService memberLoginService;

	
	public final static int EMAIL_NOT_EXISTS = 0;

	public final static int EMAIL_IS_EXISTS = 1;

	public Result registerFrom() {
		RegisterForm registerForm = new RegisterForm();
		Country country = foundationService.getCountryObj();
		registerForm.setCountry(country.getCshortname());
		Form<RegisterForm> userForm = Form.form(RegisterForm.class).fill(
				registerForm);

		return ok(views.html.member.register.register.render(userForm));
	}

	@ConductRecord
	public Result register() {
		Form<RegisterForm> regForm = Form.form(RegisterForm.class)
				.bindFromRequest();
		if (regForm.hasErrors()) {
			Logger.debug("Reg Form error: {}", regForm.errorsAsJson());
			return badRequest(views.html.member.register.register
					.render(regForm));
		}
		RegisterForm form = regForm.get();
		String email = form.getEmail().toLowerCase();
		MemberRegistration reg = new MemberRegistration(form.getEmail()
				.toLowerCase(), form.getPasswd(), form.getCountry(),
				form.isSignupNewsletter(), form.getFirstname(),
				form.getLastname());
		MemberRegistrationResult result = registration.register(reg, true,
				ContextUtils.getWebContext(Context.current()));

		if (!result.isSuccess()) {
			switch (result.getType()) {
			case MEMBER_EXISTS:
				regForm.reject("email", "member.exists");
				return badRequest(views.html.member.register.register
						.render(regForm));
			case MEMBER_NOT_ACTIVATED:
				return badRequest("Member exists, but not activated");
			default:
				return badRequest("Unknown error");
			}
		}

		MemberBase memberBase = new MemberBase();
		memberBase = mService.getMemberByMemberEmail(email,
				ContextUtils.getWebContext(Context.current()));
		memberLoginService.forceLogin(memberBase);

		return redirect(controllers.base.routes.Home.home());
	}

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
	
}
