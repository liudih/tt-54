package filters.common;

import javax.inject.Singleton;

import play.Logger;
import play.libs.F.Promise;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import play.mvc.Result;
import services.common.UUIDGenerator;
import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;

/**
 * 两种 Cookie 用来跟踪长期浏览历史、还是短期浏览。 TT_LTC (Long-Term-Cookie), TT_STC
 * (Short-Term-Cookie)。两个Cookie如果都没存在，都会会给个新的UUID。
 * 
 * <ul>
 * <li>TT_LTC: 永远不会Expire的Cookie</li>
 * <li>TT_STC: 仅限于本次浏览器还没关闭前的访问</li>
 * </ul>
 * 
 * @author kmtong
 *
 */
@Singleton
public class CookieTrackingFilter implements IFilter {

	public static final String TT_LTC = "TT_LTC";
	public static final String TT_STC = "TT_STC";
	// add by lijun
	public static final String DOMAIN = "tomtop.com";

	@Override
	public int priority() {
		return 0;
	}

	@Override
	public Promise<Result> call(Context context, FilterExecutionChain chain)
			throws Throwable {
		String ltc = null;
		String stc = null;
		if (context.request().cookie(TT_LTC) == null) {
			ltc = UUIDGenerator.createAsString();
			Logger.trace("Creating Long-Term-Cookie: {}", ltc);
			context.args.put(TT_LTC, ltc);
		}
		if (context.request().cookie(TT_STC) == null) {
			stc = UUIDGenerator.createAsString();
			Logger.trace("Creating Short-Term-Cookie: {}", stc);
			context.args.put(TT_STC, stc);
		}
		Promise<Result> result = chain.executeNext(context);

		// set cookie after the result generation
		if (ltc != null) {
			//modify by lijun
			String host = context.request().getHeader("Host");
			Logger.debug("common Host:{}", host);

			if (host != null && host.indexOf(DOMAIN) != -1) {
				Logger.debug("domain:{}", DOMAIN);
				context.response().setCookie(TT_LTC, ltc,
						10 * 365 * 24 * 60 * 60, "/", "." + DOMAIN);

			} else {
				context.response().setCookie(TT_LTC, ltc,
						10 * 365 * 24 * 60 * 60);
			}

		}
		if (stc != null) {
			context.response().setCookie(TT_STC, stc);
		}
		return result;
	}

	public static String getLongTermCookie(Context ctx) {
		Cookie c = ctx.request().cookie(TT_LTC);
		if (c != null) {
			return c.value();
		}
		// if first time, no cookie in request, but in context
		String ltc = (String) ctx.args.get(TT_LTC);
		Logger.trace("New LTC detected: {}", ltc);
		return ltc;
	}

	public static String getShortTermCookie(Context ctx) {
		Cookie c = ctx.request().cookie(TT_STC);
		if (c != null) {
			return c.value();
		}
		// if first time, no cookie in request, but in context
		String stc = (String) ctx.args.get(TT_STC);
		Logger.trace("New STC detected: {}", stc);
		return stc;
	}
}
