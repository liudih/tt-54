package controllers.mobile;

import java.io.IOException;

import javax.inject.Inject;

import play.mvc.Result;
import services.mobile.IMobileSessionService;

public class Captcha extends TokenController {

	static final int DEFAULT_KAPTCHA_LEN = 4;

	@Inject
	IMobileSessionService session;

	public Result generate() throws IOException {
		// KaptchaCache cache = KaptchaCacheBuilder.create(DEFAULT_KAPTCHA_LEN);
		// String code = cache.getActualText();
		// session.set(ICaptchaService.KAPTCHA_VARNAME, code);
		// response().setHeader("Cache-Control", "no-store, no-cache");
		// response().setHeader("Content-Type", "image/jpeg");
		// ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		// ImageIO.write(cache.getImage(), "png", buffer);
		return ok().as("image/png");
	}
}
