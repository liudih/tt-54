package valueobjects.base.captcha;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public final class KaptchaCache implements Serializable {
	private static final long serialVersionUID = 5155398147789860780L;

	String actualText;
	transient BufferedImage image;

	public KaptchaCache() {
	}

	public String getActualText() {
		return actualText;
	}

	public BufferedImage getImage() {
		return image;
	}

}