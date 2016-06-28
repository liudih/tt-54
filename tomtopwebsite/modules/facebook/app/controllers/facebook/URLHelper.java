package controllers.facebook;

import play.mvc.Http.Context;

public class URLHelper {

	public String redirectUri() {
		return routes.Facebook.returnFromFacebook().absoluteURL(
				Context.current().request());
	}

}
