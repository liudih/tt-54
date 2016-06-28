package controllers.member;

import java.util.Map;

import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import services.common.UUIDGenerator;
import services.member.IMemberEmailService;
import services.member.MemberBaseService;
import services.member.login.ILoginService;
import authenticators.member.MemberLoginAuthenticator;

import com.google.inject.Inject;

import context.ContextUtils;

public class Email extends Controller {

	@Inject
	ILoginService loginService;

	@Inject
	MemberBaseService memberBaseService;

	@Inject
	IMemberEmailService memberEmailService;

	public Result activate(String activatecode) {
		int result = memberEmailService.activation(activatecode,
				ContextUtils.getWebContext(Context.current()));
		return ok(views.html.member.email.verify.result.render(result));
	}

	@Authenticated(MemberLoginAuthenticator.class)
	public Promise<Result> verify() {

		String email = loginService.getLoginData().getEmail();
		String code = UUIDGenerator.createAsString().replace("-", "");
		String url = Context.current().request().host()
				+ controllers.member.routes.Email.activate(code).url();
		Promise<Map<String, Object>> promise = memberEmailService.asyncVerify(
				email, url, code,ContextUtils.getWebContext(Context.current()));

		return promise.map(new Function<Map<String, Object>, Result>() {

			@Override
			public Result apply(Map<String, Object> map) throws Throwable {
				int status = (int) map.get("status");

				int surplus = (int) map.get("surplus");
				return ok(views.html.member.email.verify.notify.render(status,
						email, surplus));
			}
		});

	}

}
