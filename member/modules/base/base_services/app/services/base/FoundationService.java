package services.base;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ning.http.client.Response;

import play.Logger;
import play.Play;
import play.mvc.Content;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import services.ICountryService;
import services.ICurrencyService;
import services.IDefaultSettings;
import services.IFoundationService;
import services.ILanguageService;
import services.ILoginProvider;
import services.IVhostService;
import services.IWebsiteService;
import services.base.utils.CookieUtils;
import services.common.UUIDGenerator;
import session.ISessionService;
import valueobjects.base.CommonLoginContextFactory;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import context.ContextUtils;
import context.WebContext;
import context.WebCookie;
import dto.Country;
import dto.Currency;
import dto.Language;
import dto.Website;
import extensions.InjectorInstance;

@Singleton
public class FoundationService implements IFoundationService {

	public static final String COOKIE_LANG = "TT_LANG";
	// public static final String PLAY_LANG = "PLAY_LANG";
	public static final String COOKIE_DEVICE = "TT_DEVICE";
	public static final String COOKIE_CURRENCY = "TT_CURR";
	public static final String COOKIE_COUNTRY = "TT_COUN";

	public static final String DOMAIN = ".tomtop.com";
	public static final String HOST = "tomtop.com";

	@Inject
	IDefaultSettings defaultSettings;

	@Inject
	ILanguageService langService;

	@Inject
	IWebsiteService wsService;

	@Inject
	ISessionService sessionService;

	@Inject
	ICountryService countryService;

	@Inject
	ICurrencyService currencyService;

	@Inject
	IVhostService vhostService;

	@Inject
	ILoginProvider login;

	final static String LOGIN_SESSION_NAME = "LOGIN_CONTEXT";

	public int getPlatformID() {
		Website ws = wsService.getWebsite(getSiteID());
		return ws.getIplatformid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getSiteID()
	 */
	public int getSiteID() {
		return defaultSettings.getSiteID(Context.current());
	}

	public int getSiteID(Context context) {
		return defaultSettings.getSiteID(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getSiteID(context.WebContext)
	 */
	@Override
	public int getSiteID(WebContext context) {
		return defaultSettings.getSiteID(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getLanguage()
	 */
	public int getLanguage() {
		return getLanguage(Context.current());
	}

	public int getDefaultLanguage() {
		return 1;
	}

	public int getLanguage(Context ctx) {
		int qlang = this.getLanguageFromQuery(ctx);
		if (qlang != -1) {
			return qlang;
		}
		Cookie lang = ctx.request().cookie(COOKIE_LANG);
		if (lang != null) {
			String i = lang.value();
			try {
				return Integer.valueOf(i);
			} catch (Exception e) {
				Logger.debug("Language Cookie not parsed: " + i);
			}
		}
		int dftLang = defaultSettings.getLanguageID(ctx);
		setLanguage(ctx, dftLang);
		return dftLang;
	}

	private int getLanguageFromQuery(Context ctx) {
		String langstr = ctx.request().getQueryString("lang");
		if (null != langstr) {
			langstr = langstr.toLowerCase();
			Language langObj = this.langService.getLanguageByCode(langstr);
			if (langObj != null) {
				ctx.changeLang(langObj.getCname());
				this.setLanguage(langObj.getIid());
				return langObj.getIid();
			}
		}
		return -1;
	}

	/**
	 * 与上次访问对比是否是同一个域名；
	 * 
	 * @param ctx
	 * @return
	 */
	private boolean isSameHost(Context ctx) {
		String referer = ctx.request().getHeader("Referer");
		// ~ 判断是否是第一次进入此域名如果是旧直接切换语言
		if (null != referer
				&& referer.length() > 17
				&& ctx.request().uri().length() > 17
				&& false == referer.substring(0, 17).equalsIgnoreCase(
						ctx.request().uri().substring(0, 17))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getLanguage(context.WebContext)
	 */
	@Override
	public int getLanguage(WebContext ctx) {
		WebCookie lang = ctx.cookie(COOKIE_LANG);
		if (lang != null) {
			String i = lang.value();
			try {
				return Integer.valueOf(i);
			} catch (Exception e) {
				Logger.debug("Language Cookie not parsed: " + i);
			}
		}
		int dftLang = defaultSettings.getLanguageID(ctx);
		return dftLang;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getLanguageObj()
	 */
	public Language getLanguageObj() {
		Language language = langService.getLanguage(getLanguage());
		return language;
	}

	public void setLanguage(int langId) {
		Context ctx = Context.current();
		setLanguage(ctx, langId);
	}

	public void setLanguage(Context ctx, int langId) {
		Language lang = langService.getLanguage(langId);
		if (lang != null) {
			CookieUtils.setCookie(COOKIE_LANG, Integer.toString(langId), ctx);
			CookieUtils.setCookie(Play.langCookieName(), lang.getCname(), ctx);
		} else {
			Logger.debug("Language not found: " + langId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getCurrency()
	 */
	public String getCurrency() {
		Context ctx = Context.current();
		return getCurrency(ctx);
	}

	public String getCurrency(Context ctx) {
		//LoginContext loginContext = getLoginContext(ctx);
		// ~ 添加url货币参数 fcl
		String currency = ctx.request().getQueryString("currency");
		if (currency != null) {
			currency = currency.toUpperCase();
			if (null != currencyService.getCurrencyByCode(currency)) {
				this.setCurrency(currency);
				return currency;
			}
		}
		String curr = CookieUtils.getCookie(COOKIE_CURRENCY, ctx);
		Logger.debug("crruccy -->{}---", curr);
		if (null != curr && curr.trim().length() > 0
				&& null != currencyService.getCurrencyByCode(curr)) {
			this.setCurrency(curr);
			return curr;
		}
/*		if (loginContext != null && loginContext.getCurrencyCode() != null) {
			return loginContext.getCurrencyCode();
		}*/
		return defaultSettings.getCurrency(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getCurrency(context.WebContext)
	 */
	@Override
	public String getCurrency(WebContext ctx) {
		String curr = this.getCookieValue(ctx, COOKIE_CURRENCY);
		Logger.debug("webContext curr--->{} ",curr);
		if (null != curr && curr.length() > 0) {
			return curr;
		}
		LoginContext loginContext = getLoginContext(ctx);
		if (loginContext != null && loginContext.getCurrencyCode() != null) {
			return loginContext.getCurrencyCode();
		}
		return defaultSettings.getCurrency(ctx);
	}

	private String getCookieValue(WebContext ctx, String key) {
		try {
			WebCookie cookie = ctx.cookie(key);
			if (null != cookie && cookie.value() != null
					&& cookie.value().length() > 0) {
				return cookie.value();
			}
		} catch (Exception ex) {
			Logger.error("get webContext cookie " + key + " error", ex);
		}
		return "";
	}

	private Currency getCurrencyObj() {
		String ccy = getCurrency();
		Currency currency = currencyService.getCurrencyByCode(ccy);
		if (currency == null) {
			Logger.debug("Currency not found: {}", ccy);
		}
		return currency;
	}

	public void setCurrency(String currency) {
		 Context ctx = Context.current();
		 CookieUtils.setCookie(COOKIE_CURRENCY, currency, ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getCountry()
	 */
	public String getCountry() {
		Context ctx = Context.current();
		LoginContext loginContext = getLoginContext(ctx);
		Logger.debug("loginContext == null {},{}", loginContext == null,
				loginContext.getCountryCode() == null);
		if (loginContext != null && loginContext.getCountryCode() != null) {
			return loginContext.getCountryCode();
		}
		return defaultSettings.getCountryCode(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getCountry(context.WebContext)
	 */
	@Override
	public String getCountry(WebContext ctx) {
		LoginContext loginContext = getLoginContext(ctx);
		if (loginContext != null && loginContext.getCountryCode() != null) {
			return loginContext.getCountryCode();
		}
		return defaultSettings.getCountryCode(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getCountryObj()
	 */
	public Country getCountryObj() {
		String country = getCountry();
		Country c = countryService.getCountryByShortCountryName(country);
		if (c == null) {
			Logger.debug("Country not found: {}", country);
		}
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getCountryObj(context.WebContext)
	 */
	@Override
	public Country getCountryObj(WebContext ctx) {
		String country = getCountry(ctx);
		Country c = countryService.getCountryByShortCountryName(country);
		if (c == null) {
			Logger.debug("Country not found: {}", country);
		}
		return c;
	}

	public void setCountry(String country) {
		String host = Context.current().request().getHeader("Host");
		Logger.debug("Host:{}", host);

		if (host != null && host.indexOf(HOST) != -1) {
			if (country != null) {
				Context.current().response()
						.discardCookie(COOKIE_COUNTRY, "/", DOMAIN);
				Context.current()
						.response()
						.setCookie(COOKIE_COUNTRY, country, 365 * 24 * 3600,
								"/", DOMAIN);
			}

		} else {
			if (country != null) {
				Context.current().response().discardCookie(COOKIE_COUNTRY, "/");

				Context.current()
						.response()
						.setCookie(COOKIE_COUNTRY, country, 365 * 24 * 3600,
								"/");
			}

		}

	}

	/**
	 * To check whether the user has logged in, use
	 * <code>LoginContext.isLogin()</code>
	 *
	 * @return LoginContext
	 */
	public LoginContext getLoginContext() {
		LoginContext ctx = login.getLoginContext();
		return ctx;
	}

	/**
	 * To check whether the user has logged in, use
	 * <code>LoginContext.isLogin()</code>
	 *
	 * @return LoginContext
	 */
	public LoginContext getLoginContext(Context playContext) {
		LoginContext ctx = login.getLoginContext();

		return ctx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getLoginContext(context.WebContext)
	 */
	@Override
	public LoginContext getLoginContext(WebContext webContext) {
		LoginContext ctx = login.getLoginContext(webContext);
		return ctx;
	}

	/**
	 * To be set by member login module
	 *
	 * @param lc
	 */
	public void setLoginContext(LoginContext lc) {
		sessionService.set(LOGIN_SESSION_NAME, lc);
	}

	public void setLoginContext(LoginContext lc, Context playContext) {
		sessionService.set(LOGIN_SESSION_NAME, lc, playContext);
	}

	public String getSessionID() {
		return sessionService.getSessionID();
	}

	public String getClientIP() {
		return Context.current().request().remoteAddress();
	}

	// --------- Static Method for View Access Convenience ----------------

	public static Country _getCountryObj() {
		return InjectorInstance.getInstance(FoundationService.class)
				.getCountryObj();
	}

	public static Currency _getCurrencyObj() {
		return InjectorInstance.getInstance(FoundationService.class)
				.getCurrencyObj();
	}

	public static Language _getLanguageObj() {
		return InjectorInstance.getInstance(FoundationService.class)
				.getLanguageObj();
	}

	public static String _getCurrencyUri() {
		return InjectorInstance.getInstance(FoundationService.class)
				.getCurrentUri();
	}

	public String getCurrentUri() {
		String url = Context.current().request().uri();
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.base.IFoundationService#getVhost()
	 */
	public String getVhost() {
		String vhost = Context.current().request().host();
		return vhost;
	}

	public String getDevice() {
		String vhost = this.getVhost();
		String device = vhostService.getCdevice(vhost);
		if (device == null) {
			Logger.debug("device not found: {}", vhost);
			device = defaultSettings.getDefaultDevice();
		}
		return device;
	}

	@Override
	public String getDevice(WebContext ctx) {
		String device = null;
		WebCookie d = ctx.cookie(COOKIE_DEVICE);
		if (d == null) {
			String vhost = ctx.getVhost();
			device = vhostService.getCdevice(vhost);
			if (null == device || "".equals(device)) {
				device = defaultSettings.getDefaultDevice();
			}
			return device;
		} else {
			device = d.value();
		}
		if (null == device || "".equals(device)) {
			device = defaultSettings.getDefaultDevice();
		}

		return device;
	}

	@Override
	public WebContext getWebContext() {
		WebContext wc = ContextUtils.getWebContext(Context.current());
		return wc;
	}
}
