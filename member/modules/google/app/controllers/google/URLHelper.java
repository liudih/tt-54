package controllers.google;

import play.mvc.Http.Context;

public class URLHelper {

	public String redirectUri() {
		return routes.Google.returnFromGoogle().absoluteURL(
				Context.current().request());
	}

}
