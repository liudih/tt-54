package controllers.member;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ICaptchaService;
import services.ISystemParameterService;
import services.base.FoundationService;
import services.base.utils.DateFormatUtils;
import services.base.utils.StringUtils;
import services.member.IMemberEnquiryService;
import services.member.IMemberUpdateService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import services.member.login.ILoginService;
import services.member.registration.IMemberRegistrationService;
import session.ISessionService;
import utils.SessionUtils;
import valueobjects.base.LoginContext;
import valueobjects.base.PlayLoginContextFactory;
import valueobjects.member.MemberInSession;
import valueobjects.member.MemberRegistration;
import valueobjects.member.MemberRegistrationResult;
import base.util.md5.MD5;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.primitives.Bytes;
import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import dto.Country;
import dto.member.MemberBase;
import events.member.LoginEvent;
import forms.member.login.LoginForm;
import forms.member.passwordReset.PasswordResetUpdateForm;
import forms.member.register.RegisterForm;
import forms.mobile.member.NewPasswordForm;

/**
 * 
 * @author xiaoch
 *
 */
public class ResetPassword extends Controller {

	@Inject
	IForgetPasswdBaseService forgetPasswdService;

	@Inject
	IFindPasswordService findPasswordService;

	@Inject
	IMemberUpdateService memberUpdateService;

	@Inject
	IMemberEnquiryService memberEnquiryService;

	@Inject
	FoundationService fs;

	@Inject
	ILoginService loginService;

	@Inject
	ISessionService sessionService;
	
	/**
	 * 邮件地址链接验证
	 * 
	 * @param cid
	 * @return
	 */
	public Result resetPasswordValide(String cid) {
		String email = forgetPasswdService.getEmail(cid);
		WebContext webContex = ContextUtils.getWebContext(Context.current());
		int validate = findPasswordService.resetPasswordValide(cid, webContex);
		String result = "";
		if (validate == 0) {
			// 成功
			return ok(views.html.member.password_reset.render(cid));
		} else if (validate == 1) {
			// 已经修改过
			result = "You have modified the password, the reset password link failure";
			return ok((views.html.base.public_information_show.render(result)));
		} else {
			// 超时
			result = "Retrieve password link failed!";
			return ok((views.html.base.public_information_show.render(result)));
		}

	}

	/**
	 * 忘记密码验证后保存新密码
	 * 
	 * @return
	 */
	public Result saveResetPassword() {
		Form<PasswordResetUpdateForm> userupdateForm = Form.form(
				PasswordResetUpdateForm.class).bindFromRequest();
		if (userupdateForm.hasErrors()) {
			return badRequest();
		}
		PasswordResetUpdateForm upform = userupdateForm.get();
		String cid = upform.getCid();
		String email = forgetPasswdService.getEmail(cid);
		if (!StringUtils.isEmpty(upform.getPasswd())
				&& !StringUtils.isEmpty(email)) {
			String cpassword = MD5.md5(upform.getPasswd());
			memberUpdateService.SaveMemberPasswd(email, cpassword,
					ContextUtils.getWebContext(Context.current()));// 修改密码
			forgetPasswdService.update(false, cid); // 保存到数据库buse为false,代表连接失效
		} else {
			return badRequest();
		}
		// 注销用户登录信息
		loginService.logout();
		return redirect(controllers.member.routes.Login.login(0) + "?backUrl=/");
	}

	/**
	 * 重置密码
	 */
	public Result saveNewPassword() {
		// 旧密码验证结果
		boolean isCorrect = false;
		// 修改密码结果
		boolean result = false;
		Map<String, Object> map = new HashMap<String, Object>();
		Form<NewPasswordForm> newPwdForm = Form.form(NewPasswordForm.class)
				.bindFromRequest();
		if (newPwdForm.hasErrors()) {
			return badRequest();
		}

		NewPasswordForm npform = newPwdForm.get();

		String oldPwd = npform.getOldPwd();
		String newPwd = npform.getNewPwd();

		// 获取当前上下文用户
		LoginContext loginCtx = fs.getLoginContext();
		if (!loginCtx.isLogin()) {
			Logger.error("user isn't logged in");
			return badRequest();
		}
		String email = loginCtx.getMemberID();
		if (!StringUtils.isEmpty(oldPwd) && !StringUtils.isEmpty(newPwd)) {

			isCorrect = loginService.authentication(email, oldPwd,
					ContextUtils.getWebContext(Context.current()));
			if (isCorrect) {
				String md5newPwd = MD5.md5(newPwd);
				WebContext wContext = ContextUtils.getWebContext(Context
						.current());
				result = memberUpdateService.SaveMemberPasswd(email, md5newPwd,
						wContext);// 修改密码
				if (result) {
					sessionService.destroy(wContext);
					map.put("result", "success");
				} else {
					map.put("result", "error");
				}
			} else {
				map.put("result", "verifying");
			}
		}
		return ok(Json.toJson(map));

	}

	public Result resetPasswordForm() {
		// 获取当前上下文用户
		LoginContext loginCtx = fs.getLoginContext();
		if (!loginCtx.isLogin()) {
			Logger.error("user isn't logged in");
			return badRequest();
		}
		return ok(views.html.member.new_password.render());
	}
}
