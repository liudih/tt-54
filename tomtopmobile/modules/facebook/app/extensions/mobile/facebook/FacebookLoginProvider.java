package extensions.mobile.facebook;

import play.mvc.Http.Context;
import extensions.facebook_api.login.AbstractFacebookLoginProvider;

/**
 * 
 * @author lijun
 *
 */

public class FacebookLoginProvider extends AbstractFacebookLoginProvider {

	@Override
	public int getDisplayOrder() {
		return 1;
	}

	@Override
	public String getRedirectUri() {

		String url = controllers.mobile.facebook.routes.Facebook
				.returnFromFacebook().absoluteURL(Context.current().request());

		return url;
	}

	@Override
	public String getFacebookButtonClass() {
		return "faceBook faceBookBt lineBlock";
	}

}
