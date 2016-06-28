package controllers.mobile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import play.mvc.Result;
import services.mobile.CaptchService;
import services.mobile.IMobileSessionService;
import valueobjects.base.captcha.KaptchaCache;
import valueobjects.base.captcha.KaptchaCacheBuilder;

import com.github.napp.kaptcha.Kaptcha;

public class Captcha extends TokenController {

	static final int DEFAULT_KAPTCHA_LEN = 4;

	final static Kaptcha kaptcha = new Kaptcha();

	@Inject
	IMobileSessionService session;

	public Result generate() throws IOException {
		KaptchaCache cache = KaptchaCacheBuilder.create(DEFAULT_KAPTCHA_LEN);

		String code = cache.getActualText();
		session.set(CaptchService.KAPTCHA_VARNAME, code);
		response().setHeader("Cache-Control", "no-store, no-cache");
		response().setHeader("Content-Type", "image/jpeg");
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		ImageIO.write(cache.getImage(), "png", buffer);
		return ok(buffer.toByteArray()).as("image/png");
	}
}
