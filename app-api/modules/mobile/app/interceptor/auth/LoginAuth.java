package interceptor.auth;

import javax.inject.Inject;

import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.mobile.member.LoginService;
import valuesobject.mobile.BaseJson;

public class LoginAuth extends Simple {

	final static int NEED_LOGIN = -22;

	@Inject
	LoginService loginService;

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		if (loginService.isLogin()) {
			return delegate.call(ctx);
		}
		return Promise.promise((Function0<BaseJson>) () -> {
			BaseJson json = new BaseJson();
			json.setRe(NEED_LOGIN);
			json.setMsg("required login ");
			return json;

		}).map((BaseJson result) -> ok(Json.toJson(result)));
	}

}
