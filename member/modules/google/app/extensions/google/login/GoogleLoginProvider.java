package extensions.google.login;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.login.CryptoUtils;
import valueobjects.google.UserInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import controllers.google.URLHelper;
import extensions.member.login.ILoginProvider;

@Singleton
public class GoogleLoginProvider implements ILoginProvider {

	String appId;
	String appSecret;
	ObjectMapper objectMapper;

	@Inject
	IMemberEnquiryService memberEnquiry;

	@Inject
	IMemberUpdateService memberUpdate;

	@Inject
	FoundationService foundation;

	@Inject
	CryptoUtils crypto;

	@Inject
	URLHelper urlHelper;

	public GoogleLoginProvider() {
		this.appId = Play.application().configuration()
				.getString("google.clientId");
		this.appSecret = Play.application().configuration()
				.getString("google.clientSecret");
		Logger.debug("Google ClientID {}", appId);
		this.objectMapper = new ObjectMapper();
		// this.objectMapper
		// .setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());
	}

	@Override
	public boolean isPureJS() {
		return true;
	}

	@Override
	public Html getLoginButton() {
		String raw = foundation.getSessionID();
		if (raw != null) {
			String sessionId = crypto.md5(raw);
			return views.html.google.login.login_button.render(appId,
					urlHelper.redirectUri(), sessionId);
		}
		return Html.apply("");
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
				.url("https://www.googleapis.com/plus/v1/people/me?access_token="
						+ token)
				.get()
				.map(resp -> objectMapper.convertValue(resp.asJson(),
						UserInfo.class));
	}

	public Promise<String> getAccessToken(String code, String returnUrl,
			String state) {
		String raw = foundation.getSessionID();
		String sessionId = raw != null ? crypto.md5(raw) : null;
		if (state == null || !state.equals(sessionId)) {
			throw new RuntimeException("State not matched");
		}
		String url = "https://www.googleapis.com/oauth2/v3/token";
		Map<String, String> params = Maps.newHashMap();
		params.put("client_id", appId);
		params.put("client_secret", appSecret);
		params.put("code", code);
		params.put("redirect_uri", returnUrl);
		params.put("grant_type", "authorization_code");
		return WS.client().url(url)
				.setContentType("application/x-www-form-urlencoded")
				.post(toUrlEncoded(params)).map(resp -> {
					String body = resp.getBody();
					JsonNode node = objectMapper.readTree(body);
					return node.get("access_token").asText();
				});
	}

	protected String toUrlEncoded(Map<String, String> params) {
		return URLEncodedUtils.format(
				FluentIterable
						.from(params.entrySet())
						.transform(
								e -> new BasicNameValuePair(e.getKey(), e
										.getValue())).toList(), "UTF-8");
	}
}
