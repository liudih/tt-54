package extensions.google.login;

import javax.inject.Singleton;

import play.Logger;
import play.Play;
import play.twirl.api.Html;
import services.IFoundationService;
import session.ISessionService;

import com.google.inject.Inject;

import controllers.google.URLHelper;
import extensions.member.login.ILoginprovider;

@Singleton
public class GoogleLoginProvider implements ILoginprovider {

	String appId;

	@Inject
	IFoundationService foundation;

	@Inject
	CryptoUtils crypto;

	@Inject
	ISessionService sessionService;

	@Inject
	URLHelper urlHelper;

	public GoogleLoginProvider() {
		this.appId = Play.application().configuration()
				.getString("google.clientId");
		Logger.debug("Google ClientID {}", appId);
	}

	@Override
	public int getDisplayOrder() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public boolean isPureJS() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Html getLoginButton() {
		String raw = foundation.getSessionID();
		if (raw != null) {
			String sessionId = crypto.md5(raw);
			Logger.debug("redirectUrl------------------------{}",
					urlHelper.redirectUri());
			return views.html.google.login.login_button.render(appId,
					urlHelper.redirectUri(), sessionId);
		}
		return Html.apply("");

	}

	@Override
	public String getName() {
		return "google";
	}
}
