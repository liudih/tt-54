package controllers.paypal;

import play.mvc.Http.Context;

public class URLHelper {

	public String redirectUri() {
		return routes.PayPalLogin.returnFromPayPal().absoluteURL(
				Context.current().request());
	}

}
