package controllers.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.data.Form;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.ISystemParameterService;
import services.base.utils.DateFormatUtils;
import services.dodocool.base.FoundationService;
import services.member.IMemberEnquiryService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import services.member.login.ILoginService;

import com.google.inject.Inject;

import context.ContextUtils;
import context.WebContext;
import forms.member.findPasswd.FindPasswordForm;

public class ForgetPassword extends Controller {

	@Inject
	ILoginService loginService;

	@Inject
	IMemberEnquiryService memberService;

	@Inject
	FoundationService foundationService;

	@Inject
	ISystemParameterService parameterService;

	@Inject
	IForgetPasswdBaseService forgetPasswdBaseService;

	@Inject
	IFindPasswordService findPasswordService;

	public Result forget() {
		return ok(views.html.member.forgetpassword.forget.render());
	}

	public Result sendEmail() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Form<FindPasswordForm> form = Form.form(FindPasswordForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			resultMap.put("errorCode", 2);
			resultMap.put("required", form.errorsAsJson());
			return ok(Json.toJson(resultMap));
		}

		FindPasswordForm fForm = form.get();
		String email = fForm.getEmail();

		return redirect(routes.ForgetPassword.findPasswordEmail(email));
	}

	public Result findPasswordEmail(String email) {
		String url = "http://"
				+ Context.current().request().host()
				+ controllers.member.routes.PasswordReset
						.passwordResetForm("[cid]");
		WebContext webContext = ContextUtils.getWebContext(Context.current());
		boolean result = findPasswordService.asyncFindPassword(email, url,
				webContext);
		
		return redirect(routes.ForgetPassword.findPasswordSend(email, result));
	}

	/**
	 * 找回密码发送邮件链接成功页面
	 * 
	 * @param email
	 * @return
	 */
	public Result findPasswordSend(String email, boolean result) {
		return ok(views.html.member.forgetpassword.send_email_result
				.render(email, result));
	}
}
