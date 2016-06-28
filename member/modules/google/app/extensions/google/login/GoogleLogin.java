package extensions.google.login;

import java.util.Map;

import javax.inject.Inject;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.login.CryptoUtils;
import services.member.login.ILoginOther;
import services.member.login.IUserInfo;
import valueobjects.google.UserInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import controllers.google.URLHelper;

public class GoogleLogin implements ILoginOther {

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

	@Override
	public Promise<IUserInfo> getUserInfo(String token) {
		return WS
				.client()
				.url("https://www.googleapis.com/plus/v1/people/me?access_token="
						+ token)
				.get()
				.map(resp -> objectMapper.convertValue(resp.asJson(),
						UserInfo.class));
	}

	@Override
	public Promise<String> getAccessToken(String code, String returnUrl,
			String state,String appid,String appSecret) {

		Logger.debug("-------------code:{}", code);
		Logger.debug("-------------state:{}", state);
		Logger.debug("-------------returnUrl:{}", returnUrl);

		this.objectMapper = new ObjectMapper();

		String url = "https://www.googleapis.com/oauth2/v3/token";
		Map<String, String> params = Maps.newHashMap();
		params.put("client_id", appid);
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
