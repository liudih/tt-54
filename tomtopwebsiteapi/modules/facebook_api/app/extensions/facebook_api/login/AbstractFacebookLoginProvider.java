package extensions.facebook_api.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.twirl.api.Html;
import services.IConfigService;
import valueobjects.facebook.UserInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.google.common.collect.Lists;

import extensions.member.login.ILoginProvider;

/**
 * 抽象 FacebookLoginProvider
 * 
 * @author lijun
 *
 */
public abstract class AbstractFacebookLoginProvider implements ILoginProvider {

	String appId;
	String appSecret;
	ObjectMapper objectMapper;

	private boolean isInit = false;
	
	@Inject
	IConfigService configService;

	public AbstractFacebookLoginProvider() {

		this.objectMapper = new ObjectMapper();
		this.objectMapper
				.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());
	}

	/**
	 * 实例化
	 */
	private synchronized void init() {
		if(this.isInit){
			return;
		}
		this.isInit = true;
		// 首先加载application.conf里面的配置,如果找不到就去服务端取
		try {
			this.appId = Play.application().configuration()
					.getString("facebook.appId");
			this.appSecret = Play.application().configuration()
					.getString("facebook.appSecret");
			if (appId == null || appSecret == null) {
				List<String> keys = Lists.newLinkedList();
				keys.add("facebook.appId");
				keys.add("facebook.appSecret");
				// 去服务端去配置
				Map<String, String> config = configService.getConfig(keys);
				this.appId = config.get("facebook.appId");
				this.appSecret = config.get("facebook.appSecret");
			}

			Logger.debug("Facebook AppID {}", appId);

		} catch (Exception e) {
			Logger.debug("load facebook.appId & facebook.appSecret failed", e);
		}
	}

	@Override
	public boolean isPureJS() {
		return true;
	}

	/**
	 * 设置facebook图标的class
	 * 
	 * @author lijun
	 * @return
	 */
	public abstract String getFacebookButtonClass();

	/**
	 * 获取接收Facebook发送请求的url
	 * 
	 * @author lijun
	 * @return
	 */
	public abstract String getRedirectUri();

	/**
	 * 子类如有必要可覆盖该方法
	 * 
	 * @author lijun
	 */
	@Override
	public Html getLoginButton() {
		this.init();
		if (this.appId == null || this.appSecret == null) {
			Logger.debug("找不到配置facebook.appId所以不能提供Facebook登录,请先在application.conf中配置facebook.appId和facebook.appSecret");
			return Html.apply("");
		}
		String css = this.getFacebookButtonClass();
		return views.html.facebook_api.login.login_button.render(appId,
				this.getRedirectUri(), css);
	}

	public String getAppID() {
		return this.appId;
	}

	public Promise<UserInfo> getUserInfo(String token) {
		this.init();
		return WS
				.client()
				.url("https://graph.facebook.com/v2.2/me?fields=id,name,email,first_name,middle_name,last_name,gender,link,timezone,updated_time,verified&access_token="
						+ token).get().map(resp -> {
					JsonNode feedback = resp.asJson();
					Logger.debug("getUserInfo response body:{}", feedback);
					return objectMapper.convertValue(feedback, UserInfo.class);
				});
	}

	public Promise<String> getAccessToken(String code, String returnUrl) {
		this.init();
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
