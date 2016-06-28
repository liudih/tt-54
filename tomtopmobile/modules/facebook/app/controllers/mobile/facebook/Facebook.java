package controllers.mobile.facebook;

import play.mvc.Http.Context;

import com.google.inject.Singleton;

import controllers.facebook_api.AbstractFacebook;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class Facebook extends AbstractFacebook {

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
