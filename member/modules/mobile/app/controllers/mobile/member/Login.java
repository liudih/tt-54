package controllers.mobile.member;

import javax.inject.Inject;

import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.utils.StringUtils;
import services.mobile.CaptchService;
import services.mobile.member.LoginService;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.result.LoginJson;
import valuesobject.mobile.member.result.LoginResultType;
import valuesobject.mobile.member.result.MobileMember;

import com.google.common.eventbus.EventBus;

import context.ContextUtils;
import controllers.mobile.TokenController;

public class Login extends TokenController {

	final static int REQ_LOGIN_REQUIRED_CODE_COUNT = 3;

	final static int REQUIRED_CODE = -6;

	@Inject
	CaptchService captchService;

	@Inject
	LoginService loginService;

	@Inject
	EventBus eventBus;

	public Result login(String email, String pwd, String code) {
		BaseJson resultJson = new BaseJson();
		if (StringUtils.isEmpty(email)) {
			resultJson.setRe(BaseResultType.EMAIL_IS_EMPTY_ERROR_CODE);
			resultJson.setMsg(BaseResultType.EMAIL_IS_EMPTY_ERROR_MSG);
			return ok(Json.toJson(resultJson));
		}
		if (StringUtils.isEmpty(pwd)) {
			resultJson.setRe(BaseResultType.PASSWORD_IS_EMPTY_ERROR_CODE);
			resultJson.setMsg(BaseResultType.PASSWORD_IS_EMPTY_ERROR_MSG);
			return ok(Json.toJson(resultJson));
		}
		F.Tuple<Integer, MobileMember> tuple = loginService.login(email, pwd,
				ContextUtils.getWebContext(Context.current()));
		int status = tuple._1;
		switch (status) {
		case LoginResultType.UNKNOWN:
			resultJson.setRe(BaseResultType.EMAIL_NOT_FIND_ERROR_CODE);
			resultJson.setMsg(BaseResultType.EMAIL_NOT_FIND_ERROR_MSG);
			break;
		case LoginResultType.PASSWORD_ERROR:
			resultJson.setRe(BaseResultType.PASSWORD_ERROR_CODE);
			resultJson.setMsg(BaseResultType.PASSWORD_ERROR_MSG);
			break;
		case LoginResultType.SUCCESS:
			LoginJson json = new LoginJson();
			json.setRe(LoginResultType.SUCCESS);
			json.setInfo(tuple._2);
			json.setMsg(BaseResultType.LOGIN_SUCCESS_MSG);
			return ok(Json.toJson(json));
		default:
			resultJson.setRe(BaseResultType.LOGIN_ERROR_CODE);
			resultJson.setMsg(BaseResultType.LOGIN_ERROR_MSG);
			break;
		}
		if (loginService.isCaptchas()) {
			resultJson.setRe(REQUIRED_CODE);
		}
		return ok(Json.toJson(resultJson));
	}

	public Result loginOut() {
		BaseJson resultJson = new BaseJson();
		try {
			loginService.loginOut();
		} catch (Exception e) {
			Logger.debug("mobile client loginout error:{}", e.getMessage());

			resultJson.setRe(BaseResultType.FAILURE);
			resultJson.setMsg(BaseResultType.OPERATE_FAIL);
			return ok(Json.toJson(resultJson));
		}
		resultJson.setRe(BaseResultType.SUCCESS);
		resultJson.setMsg(BaseResultType.SUCCESSMSG);
		return ok(Json.toJson(resultJson));
	}

}
