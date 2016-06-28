package services.base;

import javax.inject.Inject;

import play.Logger;
import play.mvc.Http.Context;
import services.IDefaultSettings;
import services.ILanguageService;
import services.base.geoip.GeoIPService;
import context.WebContext;
import dto.Country;
import dto.Currency;
import dto.Website;

public class DefaultSettings implements IDefaultSettings {

	final static int DEFAULT_LANG_ID = 1;
	final static String DEFAULT_CCY = "USD";
	final static String DEFAULT_COUNTRY = "US";
	final static String DEFAULT_DEVICE = "web";

	@Inject
	GeoIPService geoIPService;

	@Inject
	WebsiteService wsService;

	@Inject
	CountryService countryEnquiryService;

	@Inject
	CurrencyService currencyService;

	@Inject
	ILanguageService languageService;

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getSiteID(play.mvc.Http.Context)
	 */
	public int getSiteID(Context context) {
		Website ws = getWebsite(context);
		return ws != null ? ws.getIid() : 1;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getSiteID(context.WebContext)
	 */
	public int getSiteID(WebContext context) {
		Website ws = getWebsite(context);
		return ws != null ? ws.getIid() : 1;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getCountryCode(play.mvc.Http.Context)
	 */
	public String getCountryCode(Context context) {
		String code = geoIPService.getCountryCode(context.request()
				.remoteAddress());
		if (code != null) {
			return code;
		}
		Website ws = getWebsite(context);
		if (ws != null) {
			Country country = countryEnquiryService.getCountryByCountryId(ws
					.getIdefaultshippingcountry());
			if (country != null)
				return country.getCshortname();
		}
		return DEFAULT_COUNTRY;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getCountryCode(context.WebContext)
	 */
	public String getCountryCode(WebContext context) {
		String code = geoIPService.getCountryCode(context.getRemoteAddress());
		if (code != null) {
			return code;
		}
		Website ws = getWebsite(context);
		if (ws != null) {
			Country country = countryEnquiryService.getCountryByCountryId(ws
					.getIdefaultshippingcountry());
			if (country != null)
				return country.getCshortname();
		}
		return DEFAULT_COUNTRY;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getCurrency(play.mvc.Http.Context)
	 */
	public String getCurrency(Context context) {
		Currency currency = null;
		Country country = getCountry(context);
		if (country != null) {
			currency = currencyService
					.getCurrencyByCode(country.getCcurrency());
		}
		if (currency == null) {
			Website ws = getWebsite(context);
			if (ws != null && ws.getIcurrencyid() != null) {
				currency = currencyService.getCurrencyById(ws.getIcurrencyid());
			}
		}
		if (currency != null) {
			return currency.getCcode();
		}
		return DEFAULT_CCY;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getCurrency(context.WebContext)
	 */
	public String getCurrency(WebContext context) {
		Currency currency = null;
		Country country = getCountry(context);
		if (country != null) {
			currency = currencyService
					.getCurrencyByCode(country.getCcurrency());
		}
		if (currency == null) {
			Website ws = getWebsite(context);
			if (ws != null && ws.getIcurrencyid() != null) {
				currency = currencyService.getCurrencyById(ws.getIcurrencyid());
			}
		}
		if (currency != null) {
			return currency.getCcode();
		}
		return DEFAULT_CCY;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getLanguageID(play.mvc.Http.Context)
	 */
	public int getLanguageID(Context context) {
		Website ws = getWebsite(context);
		if (ws != null && ws.getIlanguageid() != null) {
			return ws.getIlanguageid();
		}
		String code = geoIPService.getCountryCode(context.request()
				.remoteAddress());
		if (code != null) {
			Country country = countryEnquiryService
					.getCountryByShortCountryName(code);
			if (country != null) {
				if (country.getIlanguageid() != null) {
					return country.getIlanguageid();
				}
			}
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getLanguageID(context.WebContext)
	 */
	public int getLanguageID(WebContext context) {
		Website ws = getWebsite(context);
		if (ws != null && ws.getIlanguageid() != null) {
			return ws.getIlanguageid();
		}
		String code = geoIPService.getCountryCode(context.getRemoteAddress());
		if (code != null) {
			Country country = countryEnquiryService
					.getCountryByShortCountryName(code);
			if (country != null) {
				if (country.getIlanguageid() != null) {
					return country.getIlanguageid();
				}
			}
		}
		return 1;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getDefaultLanguageID()
	 */
	public int getDefaultLanguageID() {
		return DEFAULT_LANG_ID;
	}

	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getDefaultCurrency()
	 */
	public String getDefaultCurrency() {
		return DEFAULT_CCY;
	}

	protected Website getWebsite(Context context) {
		String vhost = context.request().host();
		if (vhost.contains(":")) {
			vhost = vhost.substring(0, vhost.lastIndexOf(':'));
		}
		Logger.trace("Website: {}", vhost);
		return wsService.getWebsite(vhost);
	}

	protected Website getWebsite(WebContext context) {
		String vhost = context.getVhost();
		if (vhost.contains(":")) {
			vhost = vhost.substring(0, vhost.lastIndexOf(':'));
		}
		Logger.trace("Website: {}", vhost);
		return wsService.getWebsite(vhost);
	}

	protected Country getCountry(Context context) {
		String code = getCountryCode(context);
		Country country = countryEnquiryService
				.getCountryByShortCountryName(code);
		return country;
	}

	protected Country getCountry(WebContext context) {
		String code = getCountryCode(context);
		Country country = countryEnquiryService
				.getCountryByShortCountryName(code);
		return country;
	}
	
	/* (non-Javadoc)
	 * @see services.base.IDefaultSettings#getDefaultDevice()
	 */
	public String getDefaultDevice() {
		return DEFAULT_DEVICE;
	}
}
