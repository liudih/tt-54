package services.mobile;

import javax.inject.Inject;

public class CaptchService {

	public static final String KAPTCHA_VARNAME = "kaptcha-cache";

	@Inject
	IMobileSessionService session;

	public boolean verify(String input) {

		String captch = (String) session.get(KAPTCHA_VARNAME);
		if (captch != null && input != null) {
			return captch.equalsIgnoreCase(input);
		}
		return false;
	}
}
