package services.mobile.third;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import services.IConfigService;
import services.IDefaultSettings;
import services.member.IMemberEnquiryService;
import services.member.registration.IMemberRegistrationService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import valueobjects.member.MemberOtherIdentity;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import valuesobject.mobile.member.result.MobileMember;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import context.WebContext;
import dto.member.MemberBase;
import entity.mobile.auth.FacebookUser;
import entity.mobile.auth.GoogleUser;
import entity.mobile.auth.PaypalUser;

public class ThirdAuthService {

	private static final String FACEBOOKE = "facebook";

	private static final String PAYPAL = "paypal";

	private static final String GOOGLE = "google";

	private static Map<String, Map<String, Object>> resource = new HashMap<String, Map<String, Object>>();

	ObjectMapper objectMapper;
	ObjectMapper objectMapper2;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	IMemberRegistrationService registrationService;

	@Inject
	MobileService mobileService;

	@Inject
	LoginService loginService;

	@Inject
	IDefaultSettings defaultSettings;

	@Inject
	IConfigService configService;

	public ThirdAuthService() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper
				.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());
		this.objectMapper2 = new ObjectMapper();
	}

	public MobileMember loginAuth(MemberOtherIdentity otherId) {
		WebContext context = mobileService.getWebContext();
		MemberBase member = memberEnquiryService.getMemberByOtherIdentity(
				otherId, context);
		if (member == null) {
			String countryCode = defaultSettings.getCountryCode(context);
			MemberRegistration reg = new MemberRegistration(otherId.getEmail(),
					null, countryCode, true);
			MemberRegistrationResult flag = registrationService.register(reg,
					otherId, context);
			if (!flag.isSuccess()) {
				return null;
			}
			member = memberEnquiryService.getMemberByOtherIdentity(otherId,
					context);
		}
		MobileMember mMember = loginService.getMobileMember(member,
				otherId.getEmail());
		loginService.forceLogin(mMember, member.getIgroupid(), member);
		Logger.debug(otherId.getSource() + " auth login success , Email : "
				+ otherId.getEmail());
		return mMember;
	}

	public Promise<String> getFacebookToken(String code, String returnUrl) {
		this.init(FACEBOOKE);
		Map<String, Object> map = resource.get(FACEBOOKE);
		if (!map.isEmpty()) {
			try {
				String url = "https://graph.facebook.com/oauth/access_token?client_id="
						+ map.get("clientId")
						+ "&client_secret="
						+ map.get("clientSecret")
						+ "&code="
						+ code
						+ "&redirect_uri="
						+ URLEncoder.encode(returnUrl, "UTF-8");
				return WS.client().url(url).get().map(resp -> {
					String body = resp.getBody();
					return body.replaceFirst("access_token=([^&]*).*$", "$1");
				});
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Should not happened", e);
			}
		}
		return null;
	}

	public Promise<FacebookUser> getFacebookUser(String token) {
		return WS
				.client()
				.url("https://graph.facebook.com/v2.2/me?fields=id,name,email,first_name,middle_name,last_name,gender,link,timezone,updated_time,verified&access_token="
						+ token)
				.get()
				.map(resp -> {
					JsonNode feedback = resp.asJson();
					Logger.debug("getUserInfo response body:{}", feedback);
					return objectMapper.convertValue(feedback,
							FacebookUser.class);
				});
	}

	public Map<String, Object> init(String source) {
		Map<String, Object> map = resource.get(source);
		if (map == null || map.isEmpty()) {
			map = new HashMap<String, Object>();
			// 首先加载application.conf里面的配置,如果找不到就去服务端取
			try {

				String appId = Play.application().configuration()
						.getString("mobile." + source + ".clientId");
				String appSecret = Play.application().configuration()
						.getString("mobile." + source + ".clientSecret");
				if (appId == null || appSecret == null) {
					List<String> keys = Lists.newLinkedList();
					keys.add("mobile." + source + ".clientId");
					keys.add("mobile." + source + ".clientSecret");
					// 去服务端去配置
					Map<String, String> config = configService.getConfig(keys);
					appId = config.get("mobile." + source + ".clientId");
					appSecret = config
							.get("mobile." + source + ".clientSecret");
				}
				Logger.debug(source + " AppID {}", appId);
				map.put("clientId", appId);
				map.put("clientSecret", appSecret);
				resource.put(source, map);
			} catch (Exception e) {
				Logger.debug("load " + source + ".appId & " + source
						+ ".appSecret failed", e);
			}
		}
		return map;
	}

	public Promise<GoogleUser> getGoogleUser(String token) {
		return WS
				.client()
				.url("https://www.googleapis.com/plus/v1/people/me?access_token="
						+ token)
				.get()
				.map(resp -> objectMapper2.convertValue(resp.asJson(),
						GoogleUser.class));
	}

	public Promise<String> getGoogleToken(String code, String returnUrl,
			String state) {
		this.init(GOOGLE);
		Map<String, Object> map = resource.get(GOOGLE);
		if (state == null || !state.equals(mobileService.getTokenKey())) {
			throw new RuntimeException("State not matched");
		}
		String url = "https://www.googleapis.com/oauth2/v3/token";
		Map<String, String> params = Maps.newHashMap();
		params.put("client_id", map.get("clientId").toString());
		params.put("client_secret", map.get("clientSecret").toString());
		params.put("code", code);
		params.put("redirect_uri", returnUrl);
		params.put("grant_type", "authorization_code");
		return WS.client().url(url)
				.setContentType("application/x-www-form-urlencoded")
				.post(toUrlEncoded(params)).map(resp -> {
					String body = resp.getBody();
					JsonNode node = objectMapper2.readTree(body);
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

	public Promise<PaypalUser> getPaypalUser(String token) {
		String url = "https://" + "api.paypal.com"
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
								PaypalUser.class);
					}
					throw new RuntimeException("Cannot get user info");
				});
	}

	public Promise<String> getPaypalToken(String code, String returnUrl) {
		this.init(PAYPAL);
		Map<String, Object> map = resource.get(PAYPAL);
		try {
			String url = "https://" + "api.paypal.com"
					+ "/v1/identity/openidconnect/tokenservice?client_id="
					+ map.get("clientId") + "&client_secret="
					+ map.get("clientSecret")
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
}
