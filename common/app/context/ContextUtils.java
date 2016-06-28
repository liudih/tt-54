package context;

import play.mvc.Http.Context;
import play.mvc.Http.Request;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import filters.common.CookieTrackingFilter;

public class ContextUtils {

	private static final String WEBCONTEXT_KEY = "WebContext";

	public static WebContext getWebContext(Context ctx) {
		WebContext webContext = (WebContext) ctx.args.get(WEBCONTEXT_KEY);
		if (webContext == null) {
			Request req = ctx.request();
			webContext = new WebContext(req.remoteAddress(), req.host(),
					CookieTrackingFilter.getLongTermCookie(ctx),
					CookieTrackingFilter.getShortTermCookie(ctx),
					Lists.newArrayList(Iterators.transform(req.cookies()
							.iterator(), c -> new WebCookie(c.name(),
							c.value(), c.maxAge(), c.path(), c.domain()))));
			ctx.args.put(WEBCONTEXT_KEY, webContext);
		}
		return webContext;
	}
}
