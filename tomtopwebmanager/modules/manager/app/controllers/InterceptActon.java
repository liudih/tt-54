package controllers;

import play.data.Form;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import session.ISessionService;

import com.google.inject.Inject;

import forms.manager.login.LoginForm;

public class InterceptActon extends play.mvc.Action.Simple {

	final static String ADMIN_SESSION_NAME = "ADMIN_LOGIN_CONTEXT";
	@Inject
	ISessionService sessionService;

	public F.Promise<Result> call(Http.Context ctx) throws Throwable {
		if (sessionService.get(ADMIN_SESSION_NAME) == null) {

			Form<LoginForm> form = Form.form(LoginForm.class);
			return F.Promise.pure(ok(views.html.manager.login.render(form)));
		}
		return delegate.call(ctx);
	}
}
