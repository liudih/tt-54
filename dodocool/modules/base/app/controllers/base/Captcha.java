package controllers.base;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICaptchaService;

import com.google.common.primitives.Bytes;

import context.ContextUtils;

public class Captcha extends Controller {

	@Inject
	ICaptchaService captchaService;

	public Result generate() throws IOException {
		response().setHeader("Cache-Control", "no-store, no-cache");
		List<Byte> captcha = captchaService.createCaptcha(ContextUtils
				.getWebContext(Context.current()));
		byte[] bytes = Bytes.toArray(captcha);
		response().setHeader("Cache-Control", "no-store, no-cache");
		return ok(bytes).as("image/png");
	}

	public Result checkCaptcha() {
		if (request().body().asFormUrlEncoded().get("captcha") == null) {
			return ok(Json.toJson(false));
		}
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		Logger.debug("captcha:{}", captcha);
		boolean flag = captchaService.verify(captcha,
				ContextUtils.getWebContext(Context.current()));
		return ok(Json.toJson(flag));
	}
}
