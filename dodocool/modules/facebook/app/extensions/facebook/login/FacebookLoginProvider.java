package extensions.facebook.login;

import javax.inject.Singleton;

import play.Logger;
import play.Play;
import play.twirl.api.Html;
import services.IFoundationService;
import services.util.CryptoUtils;

import com.google.inject.Inject;

import controllers.facebook.URLHelper;
import extensions.member.login.ILoginprovider;

@Singleton
public class FacebookLoginProvider implements ILoginprovider {

	@Inject
	IFoundationService foundation;

	@Inject
	CryptoUtils crypto;

	@Inject
	URLHelper urlHelper;

	String appId;

	public FacebookLoginProvider() {
		this.appId = Play.application().configuration()
				.getString("facebook.appId");
		Logger.debug("Facebook AppID {}", appId);
	}

	@Override
	public int getDisplayOrder() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public boolean isPureJS() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Html getLoginButton() {
		Logger.debug("redirectUrl------------------------{}",
				urlHelper.redirectUri());
		return views.html.facebook.login.login_button.render(appId,
				urlHelper.redirectUri());
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "facebook";
	}

}
