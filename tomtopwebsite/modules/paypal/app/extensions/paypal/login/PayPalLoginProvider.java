package extensions.paypal.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;
import javax.inject.Singleton;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.twirl.api.Html;
import valueobjects.paypal.UserInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;

import controllers.paypal.URLHelper;
import extensions.member.login.ILoginProvider;

@Singleton
public class PayPalLoginProvider implements ILoginProvider {

	String endpoint;
	String remoteHost;
	String clientId;
	String secret;
	ObjectMapper objectMapper;

	@Inject
	URLHelper urlHelper;

	public PayPalLoginProvider() {
		Configuration config = Play.application().configuration()
				.getConfig("paypal");
		if (config != null) {
			Boolean sandbox = config.getBoolean("sandbox");
			if (sandbox != null && sandbox) {
				this.remoteHost = "www.sandbox.paypal.com";
				this.endpoint = "api.sandbox.paypal.com";
			} else {
				this.remoteHost = "www.paypal.com";
				this.endpoint = "api.paypal.com";
			}
			this.clientId = config.getString("clientid");
			this.secret = config.getString("secret");
			this.objectMapper = new ObjectMapper();
			this.objectMapper
					.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());

			Logger.debug("PayPal Client ID: {}", this.clientId);
			Logger.info("PayPal Endpoint: {}", this.endpoint);
		} else {
			Logger.error("PayPal configuration not found!");
		}
	}

	@Override
	public int getDisplayOrder() {
		return 100;
	}

	@Override
	public boolean isPureJS() {
		return false;
	}

	@Override
	public Html getLoginButton() {
		return views.html.paypal.login_button.render(clientId, remoteHost,
				urlHelper.redirectUri());
	}

	public Promise<String> getAccessToken(String code, String returnUrl) {
		try {
			String url = "https://" + endpoint
					+ "/v1/identity/openidconnect/tokenservice?client_id="
					+ clientId + "&client_secret=" + secret
					+ "&grant_type=authorization_code&code=" + code
					+ "&redirect_uri=" + URLEncoder.encode(returnUrl, "UTF-8");
			return WS.client().url(url).get().map(resp -> {
				Logger.debug("PayPal return status: {}", resp.getStatus());
				if (resp.getStatus() >= 200 && resp.getStatus() < 300) {
					JsonNode body = resp.asJson();
					Logger.trace("Return from PayPal: {}", body);
					return body.get("access_token").asText();
				}
				throw new RuntimeException("Cannot get access token");
			});
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Should not happened", e);
		}
	}

	public Promise<UserInfo> getUserInfo(String token) {
		String url = "https://" + endpoint
				+ "/v1/identity/openidconnect/userinfo?schema=openid";
		String headerValue = "Bearer " + token;
		Logger.debug("Header Value: {}", headerValue);
		return WS
				.client()
				.url(url)
				.setHeader("Authorization", headerValue)
				.setContentType("application/json")
				.get()
				.map(resp -> {
					Logger.trace("PayPal return status: {}", resp.getStatus());
					if (resp.getStatus() >= 200 && resp.getStatus() < 300) {
						Logger.trace("Response Headers: {}",
								resp.getAllHeaders());
						return objectMapper.convertValue(resp.asJson(),
								UserInfo.class);
					}
					throw new RuntimeException("Cannot get user info");
				});
	}

}
