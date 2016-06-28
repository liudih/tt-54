package controllers.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import services.dodocool.member.MemberLoginService;
import services.member.IMemberEnquiryService;
import services.member.login.ILoginService;
import session.ISessionService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.member.MemberBase;
import extensions.member.login.LoginProviderHelper;
import forms.member.login.LoginForm;
import forms.member.register.RegisterForm;

public class Login extends Controller {
	@Inject
	ILoginService loginService;

	@Inject
	ISessionService sessionService;

	@Inject
	IMemberEnquiryService memberService;

	@Inject
	MemberLoginService memberLoginService;

	@Inject
	LoginProviderHelper loginProviders;

	public Result login() {
		String header = request().getHeader("Referer");
		Form<LoginForm> form = Form.form(LoginForm.class);
		Form<RegisterForm> reg = Form.form(RegisterForm.class);

		return ok(views.html.member.login.login.render(form, reg, header, null,
				loginProviders));
	}

	public Result logining(String backUrl) {
		Map<String, List<String>> errors = null;
		Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();
		if (form.hasErrors()) {
			Form<RegisterForm> reg = Form.form(RegisterForm.class);
			Map<String, List<ValidationError>> errorMap = form.errors();
			errors = Maps.transformValues(errorMap, elist -> {
				return Lists.transform(elist, e -> e.message());
			});
			Logger.debug("errors:{}", errors);
			return badRequest(views.html.member.login.login.render(form, reg,
					backUrl, errors, loginProviders));
		}

		LoginForm login = form.get();
		Context httpCtx = ctx();
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		if (loginService.authentication(login.getEmail().toLowerCase(), login.getPassword(),
				webContext)) {
			MemberBase member = memberService.getMemberByMemberEmail(
					login.getEmail().toLowerCase(), webContext);
			memberLoginService.forceLogin(member);
			if (backUrl != null) {
				return redirect(backUrl);
			}

			return redirect(controllers.base.routes.Home.home());
		}

		if (errors == null) {
			errors = new HashMap<String, List<String>>();
		}
		List<String> errorList = new ArrayList<String>();
		errorList
				.add("The email address or password you entered is incorrect.");
		errors.put("accounterror", errorList);
		form.reject("Login Error");
		Form<RegisterForm> reg = Form.form(RegisterForm.class);
		Logger.debug("errors2:{}", errors);
		return ok(views.html.member.login.login.render(form, reg, backUrl,
				errors, loginProviders));
	}

	public Result logout() {
		Context httpCtx = ctx();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		sessionService.destroy(webCtx);

		return redirect(controllers.base.routes.Home.home());
	}

	public static Result saveReferrerBeforeLogin(String targetUrl) {
		Cookie retPath = request().cookie("RET_PATH");
		if (retPath == null) {
			String referrer = request().getHeader(Http.HeaderNames.REFERER);
			Logger.debug("Saving Referrer Before Login: {}", referrer);
			response().setCookie("RET_PATH", referrer, 300);
		}
		if ("GET".equalsIgnoreCase(request().method())) {
			String params = request().uri().substring(
					request().uri().indexOf('?'));
			return redirect(targetUrl + params);
		}
		return redirect(targetUrl);
	}
}
