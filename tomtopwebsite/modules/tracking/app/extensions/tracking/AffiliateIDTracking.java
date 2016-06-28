package extensions.tracking;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterators;

import context.WebContext;
import context.WebCookie;
import play.Logger;
import play.libs.F.Promise;
import play.mvc.Action.Simple;
import play.mvc.Http;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import play.mvc.Http.Cookies;
import play.mvc.Result;
import service.tracking.IAffiliateIDService;
import services.base.FoundationService;
import services.base.utils.CookieUtils;
import entity.tracking.Affiliate;
import extensions.filter.FilterExecutionChain;
import extensions.filter.IFilter;
import extensions.order.IOrderSourceProvider;

/**
 * 对营销跟踪编号进行截取存入cookie
 *
 * @author reason
 *
 */
@Singleton
public class AffiliateIDTracking extends Simple implements IFilter,
		IOrderSourceProvider, IAffiliateIDTracking {

	@Inject
	IAffiliateIDService affiliateIDService;

	@Inject
	FoundationService foundation;
	
	public static final String DOMAIN = "tomtop.com";

	/*
	 * (non-Javadoc)
	 * 
	 * @see extensions.tracking.IAffiliateIDTracking#priority()
	 */
	@Override
	public int priority() {
		return 20;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see extensions.tracking.IAffiliateIDTracking#call(play.mvc.Http.Context)
	 */
	@Override
	public Promise<Result> call(Context ctx) throws Throwable {
		FilterExecutionChain filterExecutionChain = null;
		return this.call(ctx, filterExecutionChain);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see extensions.tracking.IAffiliateIDTracking#call(play.mvc.Http.Context,
	 * extensions.filter.FilterExecutionChain)
	 */
	@Override
	public Promise<Result> call(Http.Context ctx, FilterExecutionChain chain)
			throws Throwable {
		try {
			String aid = ctx.request().getQueryString("aid");
			String referrer = ctx.request().getHeader("Referer");
			Cookie source = ctx.request().cookie(SOURCE_COOKIE);
			String sourceHost = null;
			int siteID = foundation.getSiteID(ctx);
			String redirectUrl = null;
			if (aid == null) {
				// prefix mapping "/aid/xxxx/remaining-url"
				String uri = ctx.request().uri();
				if (uri != null && uri.startsWith("/aid/")) {
					String[] parts = uri.split("/");
					if (parts.length >= 3) {
						aid = parts[2];
						redirectUrl = uri.substring(5 + aid.length());
						if (redirectUrl.length() == 0) {
							redirectUrl = "/";
						}
						Logger.debug("Redirecting to '{}'", redirectUrl);
					} else {
						Logger.error(
								"Invalid uri for prefixed AID extraction: {}",
								uri);
					}
				} else {
					// Check any Referer field?
					if (referrer != null && source == null) {
						URL url = new URL(referrer);
						if (url != null) {
							sourceHost = url.getHost();
							String dbaid = affiliateIDService
									.getAffiliateIDByReferer(siteID, sourceHost);
							if (dbaid != null) {
								Logger.debug("Referer: {}, AID: {}", referrer,
										dbaid);
								aid = dbaid;
							}
						}
					}
				}
			}
			if (aid != null) {
				String originalAid = aid;
				String[] aids = aid.split("-");
				if (null != aids[0]) {
					aid = aids[0];
				}
				// check if aid exists in whitelist table
				Affiliate af = affiliateIDService.getAffiliate(siteID, aid);
				if (af != null) {
					setCookie(ORIGIN_STRING, originalAid, ctx);
					Logger.debug("AffiliateID:" + aid);
				}
			}
			if (sourceHost != null && source == null) {
				ctx.response().setCookie(SOURCE_COOKIE, sourceHost);
			}
			if (redirectUrl != null) {
				return Promise.pure(redirect(redirectUrl));
			}
		} catch (Exception e) {
			Logger.error("Tracking Failed", e);
		}
		return chain == null ? null : chain.executeNext(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extensions.tracking.IAffiliateIDTracking#getSource(play.mvc.Http.Context)
	 */
	@Override
	public String getSource(Context ctx) {
		return this.getAID(ctx);
	}

	private String getAID(Context ctx) {
		String value = CookieUtils.getCookie(ORIGIN_STRING, ctx);
		Logger.debug("cookie---{}--{}", ORIGIN_STRING, value);
		return value;
	}

	@Override
	public String getSourceHost(Context ctx) {
		Cookie c = ctx.request().cookie(SOURCE_COOKIE);
		if (c != null) {
			return c.value();
		}
		return null;
	}

	@Override
	public String getAffiliateID(Context context) {
		return this.getAID(context);
	}

	@Override
	public String getAffiliateIDByWc(WebContext ctx) {
		WebCookie aidCookie = ctx.cookie(IAffiliateIDTracking.ORIGIN_STRING);
		if (aidCookie != null) {
			return aidCookie.value();
		}
		return null;
	}

	@Override
	public String getSourceHostByWc(WebContext ctx) {
		WebCookie c = ctx.cookie(SOURCE_COOKIE);
		if (c != null) {
			return c.value();
		}
		return null;
	}
	
	private static void setCookie(String key, String value, play.mvc.Http.Context context) {
		if (null == context) {
			return;
		}
		play.mvc.Http.Context ctx = context;
		String host = ctx.request().getHeader("Host");
		Logger.debug("Host:{}", host);
		if (host != null && host.indexOf(DOMAIN) != -1) {
			Logger.debug("domain:{}", DOMAIN);
			ctx.response().setCookie(key, value, 7 * 24 * 3600, "/", "." + DOMAIN);
		} else {
			ctx.response().setCookie(key, value, 7 * 24 * 3600, "/");
		}
	}

}