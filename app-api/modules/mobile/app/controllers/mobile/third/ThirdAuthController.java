package controllers.mobile.third;

import interceptor.auth.LoginAuth;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.Logger;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.With;
import services.member.IMemberEnquiryService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.third.ThirdAuthService;
import utils.MsgUtils;
import valueobjects.member.MemberOtherIdentity;
import valuesobject.mobile.BaseInfoJson;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseListJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.result.MobileMember;
import base.util.httpapi.ApiUtil;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import controllers.mobile.TokenController;
import dto.member.MemberBase;
import dto.mobile.AuthLoginInfo;
import entity.mobile.auth.FacebookUser;
import entity.mobile.auth.GoogleAppUser;
import entity.mobile.auth.GoogleUser;
import entity.mobile.auth.PaypalUser;

public class ThirdAuthController extends TokenController {

	private static String RETURN_URL = "http://app.tomtop.com/mobile/api/auth/";

	private static String GOOGLE = "https://accounts.google.com/o/oauth2/auth";

	private static String FACEBOOK = "https://www.facebook.com/dialog/oauth";

	// private static String PAYPAL =
	// "https://www.sandbox.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize";
	private static String PAYPAL = "https://www.paypal.com/webapps/auth/protocol/openidconnect/v1/authorize";
	
	// google 根据第三方应用获取用户信息
	public static final String GOOGLEURL = "https://www.googleapis.com/oauth2/v1/userinfo";

	private final static String CODE = "code";

	private final static String AUTHTOKEN = "authtoken"; //

	private final static String STATE = "state";

	private final static String SUCCESS = "success";

	private final static String FAIL = "fail";

	@Inject
	ThirdAuthService thirdAuthService;

	@Inject
	MobileService mobileService;

	@Inject
	LoginService loginService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@With(LoginAuth.class)
	public Result getAuthLoginInfo() {
		boolean islogin = loginService.isLogin();
		if (islogin) {
			String email = loginService.getLoginMemberEmail();
			if (StringUtils.isNotBlank(email)) {
				MemberBase member = memberEnquiryService
						.getMemberByMemberEmail(email.toLowerCase(),
								mobileService.getWebContext());
				if (member != null) {
					BaseInfoJson<MobileMember> result = new BaseInfoJson<MobileMember>();
					MobileMember mMember = loginService.getMobileMember(member,
							email);
					result.setRe(BaseResultType.SUCCESS);
					result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
					result.setInfo(mMember);
					return ok(Json.toJson(result));
				}
			}
		}
		BaseJson result = new BaseJson();
		result.setRe(BaseResultType.LOGIN_ERROR_CODE);
		result.setMsg(MsgUtils.msg(BaseResultType.LOGIN_ERROR_MSG));
		return ok(Json.toJson(result));
	}

	public Result getAuthInfo() {
		List<AuthLoginInfo> auths = Lists.newArrayList();
		// 拼接 google 地址
		StringBuffer gStr = new StringBuffer();
		gStr.append(GOOGLE).append("?");
		gStr.append("client_id=")
				.append(thirdAuthService.init("google").get("clientId"))
				.append("&");
		gStr.append("redirect_uri=").append(RETURN_URL + "google").append("&");
		gStr.append("response_type=").append("code").append("&");
		gStr.append("state=").append(mobileService.getTokenKey()).append("&");
		gStr.append("scope=").append("email");

		AuthLoginInfo google = new AuthLoginInfo();
		google.setName("google");
		google.setUrl(gStr.toString());
		auths.add(google);

		// 拼接 facebook 地址
		StringBuffer fbStr = new StringBuffer();
		fbStr.append(FACEBOOK).append("?");
		fbStr.append("client_id=")
				.append(thirdAuthService.init("facebook").get("clientId"))
				.append("&");
		fbStr.append("redirect_uri=").append(RETURN_URL + "facebook")
				.append("&");
		fbStr.append("response_type=").append("code").append("&");
		fbStr.append("state=").append(mobileService.getTokenKey()).append("&");
		fbStr.append("scope=").append("email");
		AuthLoginInfo fb = new AuthLoginInfo();
		fb.setName("facebook");
		fb.setUrl(fbStr.toString());
		auths.add(fb);

		// 拼接 paypal 地址
		StringBuffer ppStr = new StringBuffer();
		ppStr.append(PAYPAL).append("?");
		ppStr.append("client_id=")
				.append(thirdAuthService.init("paypal").get("clientId"))
				.append("&");
		ppStr.append("redirect_uri=").append(RETURN_URL + "paypal").append("&");
		ppStr.append("response_type=").append("code").append("&");
		ppStr.append("state=").append(mobileService.getTokenKey()).append("&");
		ppStr.append("scope=").append("openid email");

		AuthLoginInfo paypal = new AuthLoginInfo();
		paypal.setName("paypal");
		paypal.setUrl(ppStr.toString());
		auths.add(paypal);

		BaseListJson<AuthLoginInfo> result = new BaseListJson<AuthLoginInfo>();
		result.setRe(BaseResultType.SUCCESS);
		result.setMsg(MsgUtils.msg(BaseResultType.SUCCESSMSG));
		result.setList(auths);
		return ok(Json.toJson(result));
	}

	public Promise<Result> formGoogle() {
		Request request = request();
		String code = request.getQueryString(CODE);
		String state = request.getQueryString(STATE);
		try {
			if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(state)) {
				Promise<GoogleUser> userInfo = thirdAuthService
						.getGoogleToken(code, RETURN_URL + "google", state)
						.flatMap(token -> thirdAuthService.getGoogleUser(token))
						.recover(t -> {
							return null;
						});
				return userInfo.map(u -> {
					if (u == null || StringUtils.isBlank(u.getEmail())) {
						Logger.debug("No access to the user's mail !");
						return ok(views.html.mobile.auth_result.render(FAIL,
								"email is empty!"));
					}
					MemberOtherIdentity otherId = new MemberOtherIdentity(
							"google", u.getId(), u.getEmail());
					MobileMember mMember = this.authLogin(otherId);
					if (mMember != null) {
						return ok(views.html.mobile.auth_result.render(SUCCESS,
								u.getEmail()));
					}
					return ok(views.html.mobile.auth_result.render(FAIL,
							"login fail"));
				});
			}
		} catch (Exception e) {
			Logger.error("google Login Error", e.fillInStackTrace());
			return Promise.pure(ok(views.html.mobile.auth_result.render(FAIL,
					"login Error")));
		}
		return Promise.pure(ok(views.html.mobile.auth_result.render(FAIL,
				"login fail")));
	}

	public Promise<Result> formFacebook() {
		Request request = request();
		String code = request.getQueryString(CODE);
		Logger.debug("facebook sign in coming");
		try {
			if (!StringUtils.isEmpty(code)) {
				Promise<FacebookUser> userInfo = thirdAuthService
						.getFacebookToken(code, RETURN_URL + "facebook")
						.flatMap(
								token -> thirdAuthService
										.getFacebookUser(token)).recover(t -> {
							return null;
						});
				return userInfo.map(u -> {
					if (u == null || StringUtils.isBlank(u.getEmail())) {
						Logger.debug("No access to the user's mail !");
						return ok(views.html.mobile.auth_result.render(FAIL,
								"email is empty!"));
					}
					MemberOtherIdentity otherId = new MemberOtherIdentity(
							"facebook", u.getId(), u.getEmail());
					MobileMember mMember = this.authLogin(otherId);
					if (mMember != null) {
						return ok(views.html.mobile.auth_result.render(SUCCESS,
								u.getEmail()));
					}
					return ok(views.html.mobile.auth_result.render(FAIL,
							"login fail"));
				});
			}
		} catch (Exception e) {
			Logger.error("Facebook Login Error", e.fillInStackTrace());
			return Promise.pure(ok(views.html.mobile.auth_result.render(FAIL,
					"login Error")));
		}
		return Promise.pure(ok(views.html.mobile.auth_result.render(FAIL,
				"login fail")));
	}

	public Promise<Result> formPaypal() {
		String code = request().getQueryString(CODE);
		try {
			if (!StringUtils.isEmpty(code)) {
				Promise<PaypalUser> userInfo = thirdAuthService
						.getPaypalToken(code, RETURN_URL + "paypal")
						.flatMap(t -> thirdAuthService.getPaypalUser(t))
						.recover(t -> {
							return null;
						});
				return userInfo.map(u -> {
					if (u == null || StringUtils.isBlank(u.getEmail())) {
						Logger.warn("No access to the user's mail !");
						return ok(views.html.mobile.auth_result.render(FAIL,
								"email is empty!"));
					}
					MemberOtherIdentity otherId = new MemberOtherIdentity(
							"paypal", u.getUserId(), u.getEmail());
					MobileMember mMember = this.authLogin(otherId);
					if (mMember != null) {
						return ok(views.html.mobile.auth_result.render(SUCCESS,
								u.getEmail()));
					}
					return ok(views.html.mobile.auth_result.render(FAIL,
							"login fail"));
				});
			}
		} catch (Exception e) {
			Logger.error("paypal Login Error", e.fillInStackTrace());
			return Promise.pure(ok(views.html.mobile.auth_result.render(FAIL,
					"login Error")));
		}
		return Promise.pure(ok(views.html.mobile.auth_result.render(FAIL,
				"login fail")));
	}
	
	/**
	 * google login
	 * @return
	 */
	public Result googleLogin(String authtoken) {
//		String authtoken = request().getQueryString(AUTHTOKEN);
		Logger.debug("authtoken:"+authtoken);
		BaseJson re = new BaseJson();
		try {
			if (!StringUtils.isEmpty(authtoken)) {
				StringBuffer sbf = new StringBuffer(GOOGLEURL);
				sbf.append("?alt=json");
				sbf.append("&access_token=").append(authtoken);
				Logger.debug("get请求前数据：sbf="+sbf.toString());
				String resultBody = new ApiUtil().get(sbf.toString()); //get请求返回结果集
				Logger.debug("get返回数据：sbf="+resultBody);
				if (resultBody != null) {
					ObjectMapper objectMapper = new ObjectMapper();
					GoogleAppUser gauser = objectMapper.convertValue(
							objectMapper.readTree(resultBody), GoogleAppUser.class);
					Logger.debug("转换后用户信息:gauser="+JSON.toJSONString(gauser));
					if (gauser == null || StringUtils.isBlank(gauser.getEmail())) {
						Logger.warn("No access to the user's mail !");
						re.setRe(BaseResultType.ERROR);
						re.setMsg("No access to the user's mail !");
						return ok(Json.toJson(re));
					}
					MemberOtherIdentity otherId = new MemberOtherIdentity(
							"googleApi", gauser.getId(), gauser.getEmail());
					Logger.debug("登录操作前数据:otherId="+JSON.toJSONString(otherId));
					MobileMember mMember = this.authLogin(otherId);
					Logger.debug("登录操作后数据:mMember="+JSON.toJSONString(mMember));
					if (mMember != null) {
						re.setRe(BaseResultType.SUCCESS);
						re.setMsg(gauser.getId());
						return ok(Json.toJson(re));
					}
				}
			}
		} catch (Exception e) {
			Logger.error("google Login Error", e.fillInStackTrace());
		}
		re.setRe(BaseResultType.ERROR);
		re.setMsg("login error");
		return ok(Json.toJson(re));
	}
	
	public MobileMember authLogin(MemberOtherIdentity otherId) {
		if (StringUtils.isNotBlank(otherId.getEmail())
				&& StringUtils.isNotBlank(otherId.getId())) {
			return thirdAuthService.loginAuth(otherId);
		}
		return null;
	}
}
