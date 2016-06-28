package extensions.facebook.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.inject.Inject;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.login.ILoginOther;
import services.member.login.IUserInfo;
import valueobjects.facebook.UserInfo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;

import controllers.facebook.URLHelper;

public class FacebookLogin implements ILoginOther{
	
	String appId;
	String appSecret;
	ObjectMapper objectMapper;

	@Inject
	IMemberEnquiryService memberEnquiry;

	@Inject
	IMemberUpdateService memberUpdate;

	@Inject
	URLHelper urlHelper;
	
	public FacebookLogin() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());
	}

	@Override
	public Promise<String> getAccessToken(String code, String returnUrl,
			String state,String appid,String appSecret) {
		try {
			String url = "https://graph.facebook.com/oauth/access_token?client_id="
					+ appid
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

	@Override
	public Promise<IUserInfo> getUserInfo(String token) {
		return WS
				.client()
				.url("https://graph.facebook.com/v2.2/me?access_token=" + token)
				.get()
				.map(resp -> objectMapper.convertValue(resp.asJson(),
						UserInfo.class));
	}
	
}
