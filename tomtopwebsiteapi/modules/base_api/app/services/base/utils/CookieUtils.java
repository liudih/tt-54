package services.base.utils;

import play.mvc.Http.Cookie;
import play.Logger;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

public class CookieUtils {

	public static final String DOMAIN = "tomtop.com";

	public static void setCookie(String key, String value,
			play.mvc.Http.Context context) {
		if (null == context) {
			return;
		}
		play.mvc.Http.Context ctx = context;
		String host = ctx.request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(DOMAIN) != -1) {
			Logger.debug("domain:{}", DOMAIN);
			ctx.response().setCookie(key, value, 365 * 24 * 3600, "/",
					"." + DOMAIN);
		} else {
			ctx.response().setCookie(key, value, 365 * 24 * 3600, "/");
		}
	}

	public static void removeCookie(String key, play.mvc.Http.Context context) {
		if (null == context) {
			return;
		}
		play.mvc.Http.Context ctx = context;
		String host = ctx.request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(DOMAIN) != -1) {
			Logger.debug("domain:{}", DOMAIN);
			ctx.response().setCookie(key, "", 0, "/", "." + DOMAIN);
		} else {
			ctx.response().setCookie(key, "", 0, "/");
		}
	}
	
	public static String getCookie(String key, play.mvc.Http.Context context) {
		if (null == context || key==null) {
			return "";
		}
		Cookie c = context.request().cookie(key);
		if (c != null) {
			return c.value();
		}
		return "";
	}

}
