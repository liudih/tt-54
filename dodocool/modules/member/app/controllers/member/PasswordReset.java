package controllers.member;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.dodocool.base.FoundationService;
import services.member.IMemberUpdateService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import base.dodocool.utils.MD5;
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
	IForgetPasswdBaseService forgetPasswdBaseMapper;

	@Inject
	FoundationService foundationService;

	public Result passwordResetForm(String cid) {
		String email = forgetPasswdService.getEmail(cid);
		int result = findPasswordService.resetPasswordValide(cid,
				ContextUtils.getWebContext(Context.current()));
		if (result == 3) { // 验证码已失效
			return redirect(routes.PasswordReset.hasModify());
		} else if (result == 2) { // 该链接已使用过
			return redirect(routes.PasswordReset.timeout(result));
		} else if (result == 1) { // 使用时间已过期
			return redirect(routes.PasswordReset.timesout());
		}

		// 链接验证通过 转到修改密码页面
		play.data.Form<PasswordResetUpdateForm> userupdateForm = Form
				.form(PasswordResetUpdateForm.class);
		return ok(views.html.member.forgetpassword.reset_password.render(
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
		String email = upform.getEmail();
		String cid = upform.getCid();
		if (upform.getPasswd() != null && email != null) {
			int siteID = foundationService.getSiteID();
			String cpassword = MD5.md5(upform.getPasswd());
			memberUpdateService.SaveMemberPasswd(email, cpassword, ContextUtils.getWebContext(Context.current()));// 修改密码
			forgetPasswdBaseMapper.update(false, cid); // 保存到数据库buse为false,代表连接失效
		}

		return redirect(controllers.member.routes.Login.login());
	}

	/**
	 * 链接超时页面
	 */
	public Result timeout(Integer result) {
		return ok(views.html.member.forgetpassword.reset_password_link_result
				.render(2));
	}

	/**
	 * 重置密码当天超过三次页面
	 */
	public Result timesout() {
		return ok(views.html.member.forgetpassword.reset_password_link_result
				.render(2));
	}

	/**
	 * 已经重置密码，该链接失效页面
	 * 
	 * @return has-modify
	 */
	public Result hasModify() {
		return ok(views.html.member.forgetpassword.reset_password_link_result
				.render(1));
	}

}
