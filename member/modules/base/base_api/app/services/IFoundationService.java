package services;

import valueobjects.base.LoginContext;
import context.WebContext;
import dto.Country;

public interface IFoundationService {

	public int getSiteID(WebContext context);

	public int getLanguage(WebContext ctx);

	public String getCurrency(WebContext ctx);

	public String getCountry(WebContext ctx);

	public Country getCountryObj(WebContext ctx);

	public LoginContext getLoginContext(WebContext webContext);

	public String getDevice(WebContext ctx);

	public WebContext getWebContext();

	public String getSessionID();

	/**
	 * 获取当前context币种
	 * 
	 * @author lijun
	 * @return
	 */
	public String getCurrency();
}