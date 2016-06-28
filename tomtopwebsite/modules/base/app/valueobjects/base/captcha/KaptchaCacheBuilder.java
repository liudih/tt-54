package valueobjects.base.captcha;

import com.github.napp.kaptcha.Kaptcha;
import com.github.napp.kaptcha.KaptchaConfig;
import com.github.napp.kaptcha.KaptchaTextCreator;

public class KaptchaCacheBuilder {

	static {
		KaptchaConfig config = new KaptchaConfig();
		config.setHeight(30);
		config.setHeight(75);
		kaptcha = new Kaptcha();
	}

	private final static Kaptcha kaptcha;

	public static KaptchaCache create(int length) {
		KaptchaCache c = new KaptchaCache();
		c.actualText = KaptchaTextCreator.getText(length);
		c.image = kaptcha.createImage(c.actualText);
		return c;
	}

}
