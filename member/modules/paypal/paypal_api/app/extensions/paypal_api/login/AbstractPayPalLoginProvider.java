package extensions.paypal_api.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import play.Configuration;
import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.IConfigService;
import services.member.login.CryptoUtils;
import session.ISessionService;
import valueobjects.paypal_api.UserInfo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import context.ContextUtils;
import context.WebContext;
import extensions.InjectorInstance;
import extensions.member.login.ILoginProvider;

/**
 * 抽象 PayPal LoginProvider
 * 
 * @author lijun
 *
 */
public abstract class AbstractPayPalLoginProvider implements ILoginProvider {

	String endpoint;
	String remoteHost;
	String clientId;
	String secret;
	ObjectMapper objectMapper;

	private boolean isInit = false;

	IConfigService configService;

	public AbstractPayPalLoginProvider() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper
				.setPropertyNamingStrategy(new LowerCaseWithUnderscoresStrategy());
	}

	/**
	 * 实例化
	 */
	private synchronized void init() {
		if (this.isInit) {
			return;
		}
		this.isInit = true;

		try {
			this.configService = InjectorInstance.getInjector().getInstance(
					IConfigService.class);
		} catch (Exception e) {
			Logger.debug("获取不到IConfigService注入类,请先配置");
			return;
		}

		// 首先加载application.conf里面的配置,如果找不到就去服务端取
		try {
			Configuration defaultConfig = Play.application().configuration()
					.getConfig("paypal");
			Boolean sandbox = null;
			if (defaultConfig != null) {
				sandbox = defaultConfig.getBoolean("sandbox");
				
				this.clientId = defaultConfig.getString("clientid");
				this.secret = defaultConfig.getString("secret");

			}

			if (this.clientId == null || this.secret == null) {
				List<String> keys = Lists.newLinkedList();
				keys.add("paypal.clientid");
				keys.add("paypal.secret");
				keys.add("paypal.sandbox");
				// 去服务端取配置
				Map<String, String> config = configService.getConfig(keys);
				this.clientId = config.get("paypal.clientid");
				this.secret = config.get("paypal.secret");
				sandbox = Boolean.parseBoolean(config.get("paypal.sandbox"));
			}

			if (sandbox != null && sandbox) {
				this.remoteHost = "www.sandbox.paypal.com";
				this.endpoint = "api.sandbox.paypal.com";
			} else {
				this.remoteHost = "www.paypal.com";
				this.endpoint = "api.paypal.com";
			}
			
			Logger.debug("PayPal Client ID: {}", this.clientId);
			Logger.info("PayPal Endpoint: {}", this.endpoint);
			
		} catch (Exception e) {
			Logger.debug("load paypal.clientid & paypal.secret failed", e);
		}
	}

	@Override
	public boolean isPureJS() {
		return true;
	}

	/**
	 * 设置paypal图标的class
	 * 
	 * @author lijun
	 * @return
	 */
	public abstract String getPaypalButtonClass();

	/**
	 * 获取接收信息url 这个方法暂时没有用
	 * 
	 * @author lijun
	 * @return
	 */
	public String getNotifyUrl(){
		return null;
	}

	/**
	 * 认证成功后要跳转的url 
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
		if (this.clientId == null || this.secret == null) {
			Logger.debug("找不到配置paypal.clientid所以不能提供google登录,请先在application.conf中配置paypal.clientid和paypal.secret");
			return Html.apply("");
		}

		try {
			return views.html.paypal_api.login.login_button.render(clientId,
					remoteHost, this.getRedirectUri(),this.getPaypalButtonClass());
		} catch (Exception e) {
			Logger.debug("paypal登录按钮html绘制出现异常", e);
			return Html.apply("");
		}
	}

	public String getAppID() {
		return this.clientId;
	}

	public Promise<UserInfo> getUserInfo(String token) {
		this.init();
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

	public Promise<String> getAccessToken(String code, String returnUrl) {
		this.init();
		try {
			String url = "https://" + this.endpoint
					+ "/v1/identity/openidconnect/tokenservice?client_id="
					+ clientId + "&client_secret=" + this.secret
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
