package controllers.member;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.FoundationService;
import services.member.IMemberUpdateService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import services.member.login.ILoginService;
import base.util.md5.MD5;
import context.ContextUtils;
import dto.member.MemberBase;
import forms.member.passwordReset.PasswordResetUpdateForm;

public class PasswordReset extends Controller {
	@Inject
	MemberBase mbase;

	@Inject
	IForgetPasswdBaseService forgetPasswdService;

	@Inject
	IMemberUpdateService memberUpdateService;

	@Inject
	IFindPasswordService findPasswordService;

	@Inject
	FoundationService foundationService;
	
	@Inject
	ILoginService loginService;

	public Result passwordResetForm(String cid) {
		String email = forgetPasswdService.getEmail(cid);

		F.Tuple3<Integer, Integer, Integer> tuple3 = findPasswordService
				.resetPassValide(cid, ContextUtils.getWebContext(Context.current()));
		if (tuple3._1 != 0) {
			return redirect(routes.PasswordReset.hasModify());
		} else if (tuple3._3 != 0) {
			return redirect(routes.PasswordReset.timeout(cid));
		} else if (tuple3._2 != 0) {
			return redirect(routes.PasswordReset.timeout(cid));
		}

		// 链接验证通过 转到修改密码页面
		play.data.Form<PasswordResetUpdateForm> userupdateForm = Form
				.form(PasswordResetUpdateForm.class);
		return ok(views.html.member.passwordReset.password_reset.render(
				userupdateForm, email, cid));
	}

	public Result passwordReset(String backUrl) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Form<PasswordResetUpdateForm> userupdateForm = Form.form(
				PasswordResetUpdateForm.class).bindFromRequest();
		if (userupdateForm.hasErrors()) {
			Logger.debug("Reg Form error: {}", userupdateForm.errorsAsJson());
			resultMap.put("errorCode", 2);
			resultMap.put("required", userupdateForm.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}
		PasswordResetUpdateForm upform = userupdateForm.get();
//		//检查两次密码是否相同
//		String password = upform.getPasswd();
//		String confirm = upform.getConfirm_password();
		
		String cid = upform.getCid();
		if (upform.getPasswd() != null) {
			//首先检查一下token是否失效
			boolean isFail = forgetPasswdService.isFail(cid);
			if(!isFail){
				String cpassword = MD5.md5(upform.getPasswd());
				memberUpdateService.changePassword(cid, cpassword, ContextUtils.getWebContext(Context.current()));// 修改密码
				//设置token失效
				forgetPasswdService.update(false, cid); 
			}else{
				Logger.debug("this token is fail,ignore change password");
			}
		}
		// 如果有用户登录则将其注销
		loginService.logout();
		return redirect(controllers.member.routes.Login.login("/"));
	}

	/**
	 * 链接超时页面
	 */
	public Result timeout(String cid) {
		String email = forgetPasswdService.getEmail(cid);
		return ok(views.html.member.passwordReset.password_reset_session_timeout
				.render(email));
	}

	/**
	 * 重置密码当天超过三次页面
	 */
	public Result timesout() {
		return ok(views.html.member.passwordReset.password_reset_timesout
				.render());
	}

	/**
	 * 已经重置密码，该链接失效页面
	 * 
	 * @return has-modify
	 */
	public Result hasModify() {
		return ok(views.html.member.passwordReset.password_reset_has_modify
				.render());
	}

}
