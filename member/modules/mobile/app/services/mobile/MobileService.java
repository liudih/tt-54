package services.mobile;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Http.Context;
import play.mvc.Http.Request;
import utils.NumberUtils;
import valuesobject.mobile.member.MobileContext;
import valuesobject.mobile.member.Session;

import com.google.common.collect.Lists;

import context.WebContext;
import context.WebCookie;
import extensions.InjectorInstance;

public class MobileService {

	final static String LANGUAGE_ID = "ilid";

	final static String SITE_ID = "isiteid";

	final static String CURRENCY = "icurrency";

	@Inject
	IMobileSessionService sessionServer;

	public void setMobileContext(MobileContext ctx) {
		Session session = sessionServer.getSession();
		if (session == null) {
			session = new Session();
		}
		session.setMobileContext(ctx);
		session.setSessionID(ctx.getToken());
		sessionServer.setSesssion(session);
	}

	public MobileContext getMobileContext(Context context) {
		Session session = sessionServer.getSession(context);
		if (session != null) {
			return session.getMobileContext();
		}
		return null;
	}

	public MobileContext getMobileContext() {
		Session session = sessionServer.getSession();
		if (session != null) {
			return session.getMobileContext();
		}
		return null;
	}

	public String getAppName() {
		return "app";
	}

	public int getLanguageID() {
		Context ctx = Context.current();
		Request request = ctx.request();
		String strlanguageid = request.getHeader(LANGUAGE_ID);
		if (StringUtils.isBlank(strlanguageid)) {
			strlanguageid = request.getQueryString(LANGUAGE_ID);
		}
		MobileContext mobileContext = getMobileContext();
		if (StringUtils.isBlank(strlanguageid) && mobileContext != null
				&& mobileContext.getIlanguageid() > 0) {
			strlanguageid = getMobileContext().getIlanguageid() + "";
		}
		return strlanguageid == null ? 1
				: (NumberUtils.isNumeric(strlanguageid) ? Integer
						.parseInt(strlanguageid) : 1);
	}

	public int getWebSiteID() {
		Context ctx = Context.current();
		Request request = ctx.request();
		String strsiteid = request.getHeader(SITE_ID);
		if (StringUtils.isBlank(strsiteid)) {
			strsiteid = request.getQueryString(SITE_ID);
		}
		MobileContext mobileContext = getMobileContext();
		if (StringUtils.isBlank(strsiteid) && mobileContext != null
				&& mobileContext.getWebsite() > 0) {
			strsiteid = getMobileContext().getWebsite() + "";
		}
		return strsiteid == null ? 1
				: (NumberUtils.isNumeric(strsiteid) ? Integer
						.parseInt(strsiteid) : 1);
	}

	public String getCurrency() {
		Context ctx = Context.current();
		Request request = ctx.request();
		String currency = request.getHeader(CURRENCY);
		if (StringUtils.isBlank(currency)) {
			currency = request.getQueryString(CURRENCY);
		}
		return currency != null ? currency : "USD";
	}

	public String getUUID() {
		MobileContext ctx = getMobileContext();
		if (ctx != null) {
			return ctx.getUuid();
		}
		return null;
	}

	public String getTokenKey() {
		MobileContext ctx = getMobileContext();
		if (ctx != null) {
			return ctx.getToken();
		}
		return null;
	}

	public boolean token() {
		MobileContext ctx = getMobileContext();
		if (ctx != null) {
			return true;
		}
		return false;
	}

	public WebContext getWebContext() {
		MobileContext ctx = getMobileContext();
		if (ctx != null) {
			List<WebCookie> cookies = Lists.newArrayList();
			cookies.add(new WebCookie("TT_LANG", this.getLanguageID() + "",
					30 * 24 * 3600 * 1000, "", ctx.getHost()));
			cookies.add(new WebCookie("TT_DEVICE", "app",
					30 * 24 * 3600 * 1000, "", ctx.getHost()));
			WebContext context = new WebContext(ctx.getIp(), ctx.getHost(),
					sessionServer.getAuthKey(), ctx.getUuid(), cookies);
			context.setCurrencyCode(this.getCurrency());
			return context;
		}
		return getDefWebContext();
	}

	/**
	 * 默认语言为 英语， 用于其他语言取不到数据的情况下，默认去英语的数据
	 * 
	 * @return
	 */
	public WebContext getDefLangWebContext() {
		MobileContext ctx = getMobileContext();
		if (ctx != null) {
			List<WebCookie> cookies = Lists.newArrayList();
			cookies.add(new WebCookie("TT_LANG", "1", 30 * 24 * 3600 * 1000,
					"", ctx.getHost()));
			cookies.add(new WebCookie("TT_DEVICE", "app",
					30 * 24 * 3600 * 1000, "", ctx.getHost()));
			WebContext context = new WebContext(ctx.getIp(), ctx.getHost(),
					sessionServer.getAuthKey(), ctx.getUuid(), cookies);
			context.setCurrencyCode(this.getCurrency());
			return context;
		}
		return getDefWebContext();
	}

	private WebContext getDefWebContext() {
		List<WebCookie> cookies = Lists.newArrayList();
		cookies.add(new WebCookie("TT_LANG", "1", 30 * 24 * 3600 * 1000, "",
				"app.tomtop.com"));
		cookies.add(new WebCookie("TT_DEVICE", "app", 30 * 24 * 3600 * 1000,
				"", "app.tomtop.com"));
		WebContext context = new WebContext("0.0.0.0", "app.tomtop.com",
				sessionServer.getAuthKey(), "", cookies);
		context.setCurrencyCode(this.getCurrency());
		return context;
	}

	public static MobileService getInstance() {
		return InjectorInstance.getInjector().getInstance(MobileService.class);
	}
}
