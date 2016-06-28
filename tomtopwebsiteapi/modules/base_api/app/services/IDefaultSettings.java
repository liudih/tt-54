package services;

import play.mvc.Http.Context;
import context.WebContext;

public interface IDefaultSettings {

	public int getSiteID(Context context);

	public int getSiteID(WebContext context);

	public String getCountryCode(Context context);

	public String getCountryCode(WebContext context);

	public String getCurrency(Context context);

	public String getCurrency(WebContext context);

	public int getLanguageID(Context context);

	public int getLanguageID(WebContext context);

	public int getDefaultLanguageID();

	public String getDefaultCurrency();

	public String getDefaultDevice();

}