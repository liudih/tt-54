package controllers.mobile.paypal;

import play.mvc.Http.Context;

import com.google.inject.Singleton;

import controllers.paypal_api.AbstractPayPalLogin;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class PaypalLogin extends AbstractPayPalLogin {

	@Override
	public String getFailedRedirectUrl() {
		return controllers.member.routes.Login.login(0).absoluteURL(
				Context.current().request());
	}

	@Override
	public String getSucceedRedirectUrl() {
		return "/";
	}
}
