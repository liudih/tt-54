package extensions.mobile.google;

import play.mvc.Http.Context;
import extensions.google_api.login.AbstractGoogleLoginProvider;

/**
 * 
 * @author lijun
 *
 */

public class GoogleLoginProvider extends AbstractGoogleLoginProvider {

	@Override
	public int getDisplayOrder() {
		return 2;
	}

	@Override
	public String getRedirectUri() {

		String url = controllers.mobile.google.routes.Google.returnFromGoogle()
				.absoluteURL(Context.current().request());

		return url;
	}

	@Override
	public String getGoogleButtonClass() {
		return "google googleBt lineBlock";
	}

}
