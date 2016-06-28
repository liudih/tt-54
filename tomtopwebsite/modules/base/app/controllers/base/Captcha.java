package controllers.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICaptchaService;

public class Captcha extends Controller {

	@Inject
	ICaptchaService captchaService;

	public Result generate() throws IOException {
		response().setHeader("Cache-Control", "no-store, no-cache");
		ByteArrayOutputStream buffer = captchaService.createCaptcha(Context
				.current());
		return ok(buffer.toByteArray()).as("image/png");
	}

	public Result checkCaptcha() {
		if (request().body().asFormUrlEncoded().get("captcha") == null) {
			return ok(Json.toJson(false));
		}
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		boolean flag = captchaService.verify(captcha);
		return ok(Json.toJson(flag));
	}
}
