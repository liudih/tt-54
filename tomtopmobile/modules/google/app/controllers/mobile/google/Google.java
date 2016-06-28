package controllers.mobile.google;

import play.mvc.Http.Context;

import com.google.inject.Singleton;

import controllers.google_api.AbstractGoogle;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class Google extends AbstractGoogle {

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
