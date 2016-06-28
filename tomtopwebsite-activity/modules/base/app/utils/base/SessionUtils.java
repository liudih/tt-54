package utils.base;

import play.Logger;
import play.mvc.Http.Context;
import filters.common.CookieTrackingFilter;

public class SessionUtils {

	public static String getSessionID() {
		String sessionID = CookieTrackingFilter.getLongTermCookie(Context.current());
		if (sessionID == null) {
			Logger.error("TT_LTC cookie not found, is the CookieTrackingFilter active?");
		}
		return sessionID;
	}
}
