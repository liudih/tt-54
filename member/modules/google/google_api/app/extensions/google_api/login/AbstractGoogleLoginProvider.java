package extensions.google_api.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.mvc.Http.Context;
import play.twirl.api.Html;
import services.IConfigService;
import services.member.login.CryptoUtils;
import session.ISessionService;
import valueobjects.google.UserInfo;

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
 * 抽象 FacebookLoginProvider
 * 
 * @author lijun
 *
 */
public abstract class AbstractGoogleLoginProvider implements ILoginProvider {

	String appId;
	String appSecret;
	ObjectMapper objectMapper;

	private boolean isInit = false;

	
	IConfigService configService;

	ISessionService sessionService;

	CryptoUtils crypto;

	public AbstractGoogleLoginProvider() {
		this.objectMapper = new ObjectMapper();
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
			this.configService = InjectorInstance.getInjector().getInstance(IConfigService.class);
			if(this.configService == null){
				Logger.debug("获取不到IConfigService注入类,请先配置");
				return;
			}
		} catch (Exception e) {
			Logger.debug("获取不到IConfigService注入类,请先配置");
			return;
		}
		try {
			this.sessionService = InjectorInstance.getInjector().getInstance(ISessionService.class);
			if(this.sessionService == null){
				Logger.debug("获取不到ISessionService注入类,请先配置");
				return;
			}
		} catch (Exception e) {
			Logger.debug("获取不到ISessionService注入类,请先配置");
			return;
		}
		try {
			this.crypto = InjectorInstance.getInjector().getInstance(CryptoUtils.class);
			if(this.crypto == null){
				Logger.debug("获取不到CryptoUtils注入类,请先配置");
				return;
			}
		} catch (Exception e) {
			Logger.debug("获取不到CryptoUtils注入类,请先配置");
			return;
		}
		
		
		// 首先加载application.conf里面的配置,如果找不到就去服务端取
		try {
			this.appId = Play.application().configuration()
					.getString("google.clientId");
			this.appSecret = Play.application().configuration()
					.getString("google.clientSecret");
			if (appId == null || appSecret == null) {
				List<String> keys = Lists.newLinkedList();
				keys.add("google.clientId");
				keys.add("google.clientSecret");
				// 去服务端去配置
				Map<String, String> config = configService.getConfig(keys);
				this.appId = config.get("google.clientId");
				this.appSecret = config.get("google.clientSecret");
			}

			Logger.debug("google ClientID {}", appId);

		} catch (Exception e) {
			Logger.debug("load google.clientId & google.clientSecret failed", e);
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
	public abstract String getGoogleButtonClass();

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
			Logger.debug("找不到配置google.clientId所以不能提供google登录,请先在application.conf中配置google.clientId和google.clientSecret");
			return Html.apply("");
		}

		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);

		String raw = webCtx.getLtc();

		if (raw != null) {
			String sessionId = crypto.md5(raw);
			String css = this.getGoogleButtonClass();
			return views.html.google_api.login.login_button.render(appId,
					this.getRedirectUri(), sessionId, css);
		}
		return Html.apply("");
	}

	public String getAppID() {
		return this.appId;
	}

	public Promise<UserInfo> getUserInfo(String token) {
		this.init();
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
		this.init();
		Context httpCtx = Context.current();
		WebContext webCtx = ContextUtils.getWebContext(httpCtx);

		String raw = webCtx.getLtc();
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
