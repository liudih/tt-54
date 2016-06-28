package extensions.facebook.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.twirl.api.Html;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import valueobjects.facebook.UserInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;

import controllers.facebook.URLHelper;
import extensions.member.login.ILoginProvider;

@Singleton
public class FacebookLoginProvider implements ILoginProvider {

	String appId;
	String appSecret;
	ObjectMapper objectMapper;

	@Inject
	IMemberEnquiryService memberEnquiry;

	@Inject
	IMemberUpdateService memberUpdate;

	@Inject
	URLHelper urlHelper;

	public FacebookLoginProvider() {
		this.appId = Play.application().configuration()
				.getString("facebook.appId");
		this.appSecret = Play.application().configuration()
				.getString("facebook.appSecret");
		Logger.debug("Facebook AppID {}", appId);
		this.objectMapper = new ObjectMapper();
		this.objectMapper
				.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());
	}

	@Override
	public boolean isPureJS() {
		return true;
	}

	@Override
	public Html getLoginButton() {
		return views.html.facebook.login.login_button.render(appId,
				urlHelper.redirectUri());
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	public String getAppID() {
		return appId;
	}

	public Promise<UserInfo> getUserInfo(String token) {
		return WS
				.client()
				.url("https://graph.facebook.com/v2.2/me?access_token=" + token)
				.get()
				.map(resp -> objectMapper.convertValue(resp.asJson(),
						UserInfo.class));
	}

	public Promise<String> getAccessToken(String code, String returnUrl) {
		try {
			String url = "https://graph.facebook.com/oauth/access_token?client_id="
					+ appId
					+ "&client_secret="
					+ appSecret
					+ "&code="
					+ code
					+ "&redirect_uri=" + URLEncoder.encode(returnUrl, "UTF-8");
			return WS.client().url(url).get().map(resp -> {
				String body = resp.getBody();
				return body.replaceFirst("access_token=([^&]*).*$", "$1");
			});
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Should not happened", e);
		}
	}

}
