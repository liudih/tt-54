package services.base;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.Logger;
import play.mvc.Http.Context;
import play.mvc.Http.Cookie;
import services.ICountryService;
import services.ICurrencyService;
import services.IDefaultSettings;
import services.ILanguageService;
import services.IVhostService;
import services.IWebsiteService;
import services.base.utils.CookieUtils;
import services.member.login.ILoginService;
import session.ISessionService;
import valueobjects.base.CommonLoginContextFactory;
import valueobjects.base.LoginContext;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import context.ContextUtils;
import context.WebContext;
import context.WebCookie;
import dto.Country;
import dto.Currency;
import dto.Language;
import dto.Website;
import extensions.InjectorInstance;
import filters.common.CookieTrackingFilter;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class FoundationService {
	
	public static final String COOKIE_LANG = "TT_LANG";
	// public static final String PLAY_LANG = "PLAY_LANG";
	public static final String COOKIE_DEVICE = "TT_DEVICE";
	public static final String COOKIE_CURRENCY = "TT_CURR";
	public static final String COOKIE_COUNTRY = "TT_COUN";
	
	public static final String DEVICE_NAME = "mobile";
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
	ILoginService loginService;

	public final static String LOGIN_SESSION_NAME = "LOGIN_CONTEXT";

	// 所有国家
	private List<Country> country;

	private Map<Integer, Country> countryMap;

	private Integer site;

	public int getPlatformID() {
		Website ws = wsService.getWebsite(getSiteID());
		return ws.getIplatformid();
	}

	/**
	 * 获取SiteID
	 * 
	 * @return
	 */
	public synchronized int getSiteID() {
		if (site != null) {
			return this.site;
		}
		Context httpCtx = Context.current();

		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		this.site = defaultSettings.getSiteID(webCtx);
		return this.site;
	}

	public int getDefaultLanguage() {
		return 1;
	}

	/**
	 * 获取Language id
	 * 
	 * @return
	 */
	public int getLanguage() {
		Context ctx = Context.current();
		Cookie lang = ctx.request().cookie(COOKIE_LANG);
		if (lang != null) {
			String i = lang.value();
			try {
				return Integer.valueOf(i);
			} catch (Exception e) {
				Logger.debug("Language Cookie not parsed: " + i);
			}
		}
		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);

		int dftLang = defaultSettings.getLanguageID(webCtx);
		setLanguage(ctx, dftLang);
		return dftLang;
	}

	/**
	 * 获取Language
	 * 
	 * @return
	 */
	public Language getLanguageObj() {
		Language language = langService.getLanguage(getLanguage());
		return language;
	}

	/**
	 * 设置当前用户语言到Cookie
	 * 
	 * @param langId
	 */
	public void setLanguage(int langId) {
		Context ctx = Context.current();
		setLanguage(ctx, langId);
	}

	/**
	 * 设置当前用户语言到Cookie
	 * 
	 * @param ctx
	 * @param langId
	 */
	public void setLanguage(Context ctx, int langId) {
		// set to cookie, change play language context, affect framework
		// localization
		Language lang = langService.getLanguage(langId);
		if (lang != null) {
			ctx.response().setCookie(COOKIE_LANG, Integer.toString(langId));
			ctx.clearLang();
			ctx.changeLang(lang.getCname());
		} else {
			Logger.debug("Language not found: " + langId);
		}
	}

	/**
	 * 获取货币
	 * 
	 * @return
	 */
	public String getCurrency() {
		Context ctx = Context.current();
		return getCurrency(ctx);
	}
	
	public String getCurrency(Context ctx) {
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
		WebContext webCtx = ContextUtils.getWebContext(ctx);
		return defaultSettings.getCurrency(webCtx);
	}

	/**
	 * 获取货比对象
	 * 
	 * @return
	 */
	public Currency getCurrencyObj() {
		String ccy = getCurrency();
		Currency currency = currencyService.getCurrencyByCode(ccy);
		if (currency == null) {
			Logger.debug("Currency not found: {}", ccy);
		}
		return currency;
	}

	/**
	 * 设置当前登录用户的使用货币
	 * 
	 * @param ccy
	 */
	public void setCurrency(String currency) {
		 Context ctx = Context.current();
		 CookieUtils.setCookie(COOKIE_CURRENCY, currency, ctx);
	}

	/**
	 * 获取国家
	 * 
	 * @return
	 */
	public String getCountry() {
		LoginContext loginContext = getLoginContext();
		if (loginContext != null && loginContext.getCountryCode() != null) {
			return loginContext.getCountryCode();
		}
		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		return defaultSettings.getCountryCode(webCtx);
	}

	/**
	 * 获取国家对象
	 * 
	 * @return
	 */
	public Country getCountryObj() {
		String country = getCountry();
		Country c = countryService.getCountryByShortCountryName(country);
		if (c == null) {
			Logger.debug("Country not found: {}", country);
		}
		return c;
	}

	/**
	 * 设置当前用户的国家
	 * 
	 * @param countryCode
	 */
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
//		Context httpCtx = Context.current();
//		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
//
//		LoginContext ctx = (LoginContext) sessionService.get(
//				LOGIN_SESSION_NAME, webCtx);
//		if (ctx == null) {
//			ctx = CommonLoginContextFactory.newAnonymousLoginContext(webCtx);
//			sessionService.set(LOGIN_SESSION_NAME, ctx, webCtx);
//		}
//		return ctx;
		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		LoginContext ctx = null;
		
		Cookie uuid = Context.current().request().cookie("TT_UUID");
		Cookie token = Context.current().request().cookie("TT_TOKEN");
		if(uuid == null || token == null){
			ctx = CommonLoginContextFactory.newAnonymousLoginContext(webCtx);
			return ctx;
		}
		
		ctx = this.loginService.getLoginContext(uuid.value(),token.value());
		return ctx;
	}

	/**
	 * 设置LoginContext
	 * 
	 * @param lc
	 */
	public void setLoginContext(LoginContext lc) {
		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);
		sessionService.set(LOGIN_SESSION_NAME, lc, webCtx);
	}

	/**
	 * 获取SessionID
	 * 
	 * @return
	 */
	public String getSessionID() {
		String sessionID = CookieTrackingFilter.getLongTermCookie(Context
				.current());
		if (sessionID == null) {
			Logger.error("TT_LTC cookie not found, is the CookieTrackingFilter active?");
		}
		return sessionID;
	}

	/**
	 * 获取用户ip
	 * 
	 * @return
	 */
	public String getClientIP() {
		return Context.current().request().remoteAddress();
	}

	/**
	 * 获取Country
	 * 
	 * @author lijun
	 * @param countryId
	 * @return if not find return null
	 */
	public Country getCountry(Integer countryId) {
		if (this.countryMap == null) {
			this.initCountries();
		}
		return this.countryMap.get(countryId);
	}

	/**
	 * 获取CountryMap
	 * 
	 * @author lijun
	 * @return
	 */
	public Map<Integer, Country> getCountryMap() {
		if (this.countryMap == null) {
			this.initCountries();
		}
		return this.countryMap;
	}

	/**
	 * 获取服务端的所有国家
	 * 
	 * @return not return null
	 */
	public List<Country> getAllCountries() {
		if (this.country == null) {
			this.initCountries();
		}
		return this.country;
	}

	private synchronized void initCountries() {
		if (this.country != null) {
			return;
		}
		try {
			List<Country> countrys = countryService.getAllCountries();
			// 按首字母排序
			countrys = FluentIterable.from(countrys).toSortedList(
					new Comparator<Country>() {

						@Override
						public int compare(Country c1, Country c2) {
							char[] c1s = c1.getCname().toLowerCase()
									.toCharArray();
							char[] c2s = c2.getCname().toLowerCase()
									.toCharArray();
							if (c1s[0] > c2s[0]) {
								return 1;
							} else if (c1s[0] < c2s[0]) {
								return -1;
							} else {
								return 0;
							}
						}

					});

			if (countrys != null) {
				this.country = countrys;
				this.countryMap = Maps.uniqueIndex(countrys, c -> c.getIid());
			} else {
				this.country = Lists.newLinkedList();
				this.countryMap = Maps.newHashMap();
			}
		} catch (Exception e) {
			Logger.error("get remote all countries failed", e);
			this.country = Lists.newLinkedList();
			this.countryMap = Maps.newHashMap();
		}
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
		Context httpCtx = Context.current();
		WebContext ctx = ContextUtils.getWebContext(httpCtx);
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

	public WebContext getWebContext() {
		return ContextUtils.getWebContext(Context.current());
	}
}
