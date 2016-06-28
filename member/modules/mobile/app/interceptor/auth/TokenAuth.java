package interceptor.auth;

import javax.inject.Inject;

import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.mobile.MobileService;
import valuesobject.mobile.BaseJson;

public class TokenAuth extends Simple {

	@Inject
	MobileService mobileService;

	final int error = -99;

	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		if (mobileService.token()) {
			return delegate.call(ctx);
		}
		return Promise.promise(() -> result()).map(
				(BaseJson result) -> ok(Json.toJson(result)));
	}

	private BaseJson result() {
		BaseJson json = new BaseJson();
		json.setRe(error);
		json.setMsg("invalid request");
		return json;
	}

}
