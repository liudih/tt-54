package controllers.mobile.member;

import interceptor.auth.LoginAuth;

import javax.inject.Inject;

import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.With;
import services.member.IMemberEnquiryService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import services.member.login.CryptoUtils;
import services.mobile.CaptchService;
import services.mobile.MobileService;
import services.mobile.member.LoginService;
import services.mobile.member.PasswordService;
import valuesobject.mobile.BaseJson;
import valuesobject.mobile.BaseResultType;
import context.ContextUtils;
import controllers.mobile.TokenController;
import dto.member.ForgetPasswdBase;
import dto.member.MemberBase;
import forms.mobile.AlterPwdForm;
import forms.mobile.ResetPwdForm;

public class Password extends TokenController {

	@Inject
	IMemberEnquiryService menberService;

	@Inject
	IFindPasswordService findPasswordService;

	@Inject
	CaptchService captchService;

	@Inject
	LoginService loginService;

	@Inject
	PasswordService passwordService;

	@Inject
	IForgetPasswdBaseService forgetPwdService;

	@Inject
	MobileService mobileService;

	@Inject
	CryptoUtils crypto;

	final static int CODE_ERROR = -1;

	final static int UNKNOWN_EMAIL = -2;

	final static int EMAIL_ACTIVATED = -3;

	final static int OLD_PWD_ERROR = -4;

	public Promise<Result> find(String email) {
		BaseJson json = new BaseJson();
		MemberBase member = menberService.getMemberByMemberEmail(email,
				ContextUtils.getWebContext(Context.current()));
		if (member == null) {
			return Promise.promise(new Function0<Result>() {
				public Result apply() {
					json.setRe(BaseResultType.EMAIL_NOT_FIND_ERROR_CODE);
					json.setMsg(BaseResultType.EMAIL_NOT_FIND_ERROR_MSG);
					return ok(Json.toJson(json));
				}
			});
		}
		String url = "http://"
				+ Context.current().request().host()
				+ controllers.member.routes.PasswordReset
						.passwordResetForm("[cid]");
		return findPasswordService.asyncFindPass(email, url,
				ContextUtils.getWebContext(Context.current())).map(t -> {
			if (t._1 == 1) {
				json.setRe(BaseResultType.SUCCESS);
				json.setMsg(BaseResultType.SUCCESSMSG);
				return ok(Json.toJson(json));
			} else {
				json.setRe(BaseResultType.FAILURE);
				json.setMsg(BaseResultType.OPERATE_FAIL);
				return ok(Json.toJson(json));
			}

		});

	}

	public Result alter() {
		Form<AlterPwdForm> fm = Form.form(AlterPwdForm.class).bindFromRequest();
		BaseJson json = new BaseJson();
		if (fm.hasErrors()) {
			json.setRe(BaseResultType.PASSWORD_ALERT_INPUT_ERROR_CODE);
			json.setMsg(BaseResultType.PASSWORD_ALERT_INPUT_ERROR_MSG);
			return ok(Json.toJson(json));
		}
		AlterPwdForm mfrm = fm.get();
		String pwd = mfrm.getPwd();
		String code = mfrm.getCode();
		String email = mfrm.getEmail();
		ForgetPasswdBase forgetPwd = forgetPwdService.getForgetPwdByCode(
				email.trim(), code.trim(), true);
		if (forgetPwd == null) {
			json.setRe(BaseResultType.PASSORD_CODE_ERROR_CODE);
			json.setMsg(BaseResultType.PASSORD_CODE_ERROR_MSG);
			return ok(Json.toJson(json));
		}

		Integer websiteId = mobileService.getWebSiteID();
		boolean result = passwordService.alterPwd(email, pwd, websiteId);
		if (result) {
			forgetPwdService.deleteByEmail(email,
					ContextUtils.getWebContext(Context.current()));
			json.setRe(BaseResultType.SUCCESS);
			json.setMsg(BaseResultType.SUCCESSMSG);
			return ok(Json.toJson(json));
		}
		json.setRe(BaseResultType.FAILURE);
		json.setMsg(BaseResultType.OPERATE_FAIL);
		return ok(Json.toJson(json));

	}

	@With(LoginAuth.class)
	public Result reset() {
		Form<ResetPwdForm> fm = Form.form(ResetPwdForm.class).bindFromRequest();
		BaseJson json = new BaseJson();
		if (fm.hasErrors()) {
			json.setRe(BaseResultType.PASSWORD_RESET_INPUT_ERROR_CODE);
			json.setMsg(BaseResultType.PASSWORD_RESET_INPUT_ERROR_MSG);
			return ok(Json.toJson(json));
		}
		ResetPwdForm form = fm.get();
		String newpwd = form.getPwd();
		String oldpwd = form.getOldpwd();
		String email = loginService.getLoginMemberEmail();
		boolean result = passwordService.reset(email, newpwd, oldpwd,
				ContextUtils.getWebContext(Context.current()));
		if (result) {
			json.setRe(BaseResultType.SUCCESS);
			json.setMsg(BaseResultType.SUCCESSMSG);
			return ok(Json.toJson(json));
		}
		json.setRe(BaseResultType.OLD_PASSWORD_ERROR_CODE);
		json.setMsg(BaseResultType.OLD_PASSWORD_ERROR_MSG);
		return ok(Json.toJson(json));
	}

}
