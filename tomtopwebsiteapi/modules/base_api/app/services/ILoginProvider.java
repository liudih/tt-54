package services;

import context.WebContext;
import valueobjects.base.LoginContext;

/**
 * 
 * @author lijun
 *
 */
public interface ILoginProvider {

	public LoginContext getLoginContext();
	
	public LoginContext getLoginContext(WebContext webContext);
}
