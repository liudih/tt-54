package controllers.mobile.member;

import javax.inject.Inject;

import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.geoip.GeoIPService;
import services.member.registration.MemberRegistrationService;
import services.mobile.member.LoginService;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;
import valuesobject.mobile.member.result.LoginJson;
import valuesobject.mobile.member.result.LoginResultType;
import valuesobject.mobile.member.result.MobileMember;
import valuesobject.mobile.member.result.RegisterResultType;
import context.ContextUtils;
import controllers.mobile.TokenController;
import forms.mobile.MobileRegisterForm;

public class Register extends TokenController {

	@Inject
	MemberRegistrationService registration;

	@Inject
	GeoIPService geoIPService;

	@Inject
	LoginService loginService;

	final static String DEF_COUNTRY_CODE = "US";

	public Result register() {
		Form<MobileRegisterForm> regForm = Form.form(MobileRegisterForm.class)
				.bindFromRequest();
		BaseJson resultJson = new BaseJson();
		if (regForm.hasErrors()) {
			resultJson.setRe(BaseResultType.REGISTER_INPUT_ERROR_CODE);
			resultJson.setMsg(BaseResultType.REGISTER_INPUT_ERROR_MSG);
			return ok(Json.toJson(resultJson));
		}
		MobileRegisterForm form = regForm.get();
		String remoteAddress = request().remoteAddress();
		String countryCode = geoIPService.getCountryCode(remoteAddress);
		String code = countryCode == null ? DEF_COUNTRY_CODE : countryCode;
		MemberRegistration reg = new MemberRegistration(form.getEmail(),
				form.getPwd(), code, form.getPost() == 0 ? false : true);
		MemberRegistrationResult result = registration.register(reg, false,
				ContextUtils.getWebContext(Context.current()));
		if (!result.isSuccess()) {
			switch (result.getType()) {
			case MEMBER_EXISTS:
				resultJson
						.setRe(BaseResultType.REGISTER_USER_EXISTS_ERROR_CODE);
				resultJson
						.setMsg(BaseResultType.REGISTER_USER_EXISTS_ERROR_MSG);
				break;
			case MEMBER_NOT_ACTIVATED:
				resultJson
						.setRe(BaseResultType.REGISTER_USER_UN_ACTIVATED_ERROR_CODE);
				resultJson
						.setMsg(BaseResultType.REGISTER_USER_UN_ACTIVATED_ERROR_MSG);
				break;
			default:
				resultJson.setRe(BaseResultType.REGISTER_SERVICE_ERROR_CODE);
				resultJson.setMsg(BaseResultType.REGISTER_SERVICE_ERROR_MSG);
			}
			return ok(Json.toJson(resultJson));
		} else {
			F.Tuple<Integer, MobileMember> tuple = loginService.login(
					form.getEmail(), form.getPwd(),
					ContextUtils.getWebContext(Context.current()));
			int status = tuple._1;
			if (LoginResultType.SUCCESS == status) {
				LoginJson json = new LoginJson();
				json.setRe(LoginResultType.SUCCESS);
				json.setInfo(tuple._2);
				json.setMsg(BaseResultType.LOGIN_SUCCESS_MSG);
				return ok(Json.toJson(json));
			}
		}
		resultJson.setRe(RegisterResultType.SUCCESS);
		resultJson.setMsg(BaseResultType.REGISTER_SUCCESS_MSG);
		return ok(Json.toJson(resultJson));
	}
}
