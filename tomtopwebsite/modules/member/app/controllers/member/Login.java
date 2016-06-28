package controllers.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import services.member.login.ILoginService;
import services.member.login.LoginServiceV2;
import valueobjects.member.MemberInSession;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import context.ContextUtils;
import extensions.member.IMyMessageProvider;
import forms.member.login.LoginForm;
import forms.member.register.RegisterForm;

import com.fasterxml.jackson.databind.JsonNode;

public class Login extends Controller {

	@Inject
	ILoginService loginService;
	
	@Inject
	FoundationService foundation;
	
	@Inject
	private Set<IMyMessageProvider> mmpService;
	
	//add by lijun
	@Inject
	LoginServiceV2 loginServiceV2;
	
	public Result loginForm(String backUrl) {
		if (backUrl == null) {
			backUrl = request().getHeader("Referer");
		}

		Form<LoginForm> form = Form.form(LoginForm.class);
		Form<RegisterForm> reg = Form.form(RegisterForm.class);
		if (StringUtils.isEmpty(backUrl)) {
			response().setCookie("RET_PATH", "/", 60);
		} else {
			response().setCookie("RET_PATH", backUrl, 60);
		}
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		return ok(views.html.member.login.index.render(form, reg, loginButtons,
				backUrl, null));
	}

	public Result logout() {
		// 切换账户时将当前用户的购物车的优惠信息清除
		Context ctx = Context.current();
		if (ctx.request().cookie("loyalty") != null) {
			CookieUtils.removeCookie("loyalty", ctx);
		}
		if (ctx.request().cookie("point") != null) {
			CookieUtils.removeCookie("point", ctx);
		}
		//loginService.logout();
		loginServiceV2.logout();
		return redirect(controllers.base.routes.Home.home());
	}

	public Result login(String backUrl) {
		Logger.debug("*********************************************");
		Map<String, List<String>> errors = null;
		Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();
		List<Html> loginButtons = loginService.getOtherLoginButtons();
		if (form.hasErrors()) {
			Form<RegisterForm> reg = Form.form(RegisterForm.class);
			Map<String, List<ValidationError>> errorMap = form.errors();
			errors = Maps.transformValues(errorMap, elist -> {
				return Lists.transform(elist, e -> e.message());
			});

			return badRequest(views.html.member.login.index.render(form, reg,
					loginButtons, backUrl, errors));
		}

		LoginForm login = form.get();

		//modify by lijun
		if (loginServiceV2.login(login.getEmail(), login.getPassword())) {
			Logger.debug("*******************Login v2 Success: {}", login.getEmail());
			//生产token
			
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
		return ok(views.html.member.login.index.render(form, reg, loginButtons,
				backUrl, errors));
	}

	public Result loginAjax(String backUrl) {
		Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();
		Map<String, String> returnjson = new HashMap<String, String>();
		if (form.hasErrors()) {
			returnjson.put("result", "error");
			return ok(Json.toJson(returnjson));
		}
		LoginForm login = form.get();
		if (loginServiceV2.login(login.getEmail(), login.getPassword())) {
			returnjson.put("result", "success");
			returnjson.put("backUrl", "");
			if (backUrl != null) {
				returnjson.put("backUrl", backUrl);
			}
			return ok(Json.toJson(returnjson));
		}
		returnjson.put("result", "accountwrong");
		return ok(Json.toJson(returnjson));
	}

	public Result checkSign() {
		MemberInSession session = loginService.getLoginData();
		if (session == null) {
			return ok(Json.toJson(false));
		}
		return ok(Json.toJson(true));
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

	public static Result redirectAfterLogin() {
		Cookie referrer = request().cookie("RET_PATH");
		if (referrer != null) {
			response().discardCookie("RET_PATH");
			String referrerUrl = referrer.value();
			if (!StringUtils.isEmpty(referrerUrl)) {
				return redirect(referrerUrl);
			}
		}
		return redirect("/");
	}
	
	/**
	 * 获取登录部分html
	 * @return
	 */
	public Result getLoginSectionHtml() {
		Map<String, String> returnJson = new HashMap<String, String>();
		String html = "";
		if (foundation.getLoginContext().isLogin()) {
			Integer total = null;
			Iterator<IMyMessageProvider> iterator = mmpService.iterator();
			while (iterator.hasNext()) {
				IMyMessageProvider provider = iterator.next();
				if (total == null) {
					total = provider.getUnreadTotal();
				} else {
					total = total + provider.getUnreadTotal();
				}
			}
			html = views.html.member.navigation.user_new
					.render((MemberInSession) foundation.getLoginContext().getPayload(), total).toString();
			returnJson.put("html", html);
		} else {
			List<Html> loginButtons = loginService.getOtherLoginButtons();
			html = views.html.member.navigation.anonymous_new.render(loginButtons).toString();
			returnJson.put("html", html);
		}
		String callback = request().getQueryString("jsoncallback");
		JsonNode result = Json.toJson(returnJson);
		String resultString = String.valueOf(result);
		callback = callback + "(" + resultString + ")";
		return ok(callback);
	}
}
