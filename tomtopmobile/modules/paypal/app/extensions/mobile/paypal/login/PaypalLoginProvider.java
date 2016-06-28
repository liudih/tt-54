package extensions.mobile.paypal.login;

import play.mvc.Http.Context;
import extensions.paypal_api.login.AbstractPayPalLoginProvider;

/**
 * 
 * @author lijun
 *
 */

public class PaypalLoginProvider extends AbstractPayPalLoginProvider {

	@Override
	public int getDisplayOrder() {
		return 3;
	}

	@Override
	public String getRedirectUri() {

		String url = controllers.mobile.paypal.routes.PaypalLogin
				.returnFromPayPal().absoluteURL(Context.current().request());

		return url;
	}

	@Override
	public String getPaypalButtonClass() {
		return "paypal paypalBt lineBlock";
	}

}
