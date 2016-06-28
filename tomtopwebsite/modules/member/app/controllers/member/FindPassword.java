package controllers.member;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.base.utils.DateFormatUtils;
import services.member.MemberBaseService;
import services.member.findpassword.IFindPasswordService;
import services.member.forgetpassword.IForgetPasswdBaseService;
import services.member.login.ILoginService;
import context.ContextUtils;
import forms.member.findPasswd.FindPasswordForm;

public class FindPassword extends Controller {

	@Inject
	IFindPasswordService findPasswordService;

	@Inject
	MemberBaseService memberBaseService;

	@Inject
	SystemParameterService parameterService;

	@Inject
	IForgetPasswdBaseService forgetPasswdBaseService;

	@Inject
	FoundationService foundationService;
	
	@Inject
	ILoginService loginService;

	public Result findPasswordForm() {
		// 如果有用户登录则将其注销
		loginService.logout();
		return ok(views.html.member.findPassword.find_password.render());
	}

	public Result findPassword() {
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
		Integer siteId = foundationService.getSiteID();
		String dayNums = parameterService.getSystemParameter(siteId,
				foundationService.getLanguage(), "FindPassValideNums");
		int dayNum = Integer.valueOf(dayNums).intValue();
		List<Date> dateList = DateFormatUtils.getNowDayRange(0);
		int nums = forgetPasswdBaseService.getCount(email, dateList.get(0),
				dateList.get(1), ContextUtils.getWebContext(Context.current()));
		if (nums >= dayNum) {
			return redirect(routes.PasswordReset.timesout());
		}

		return redirect(routes.FindPassword.findPasswordEmail(email));
	}

	public Promise<Result> findPasswordEmail(String email) {
		String url = "http://"
				+ Context.current().request().host()
				+ controllers.member.routes.PasswordReset
						.passwordResetForm("[cid]");
		Promise<F.Tuple<Integer, Integer>> promise = findPasswordService
				.asyncFindPass(email, url,
						ContextUtils.getWebContext(Context.current()));
		return promise.map(map -> redirect(routes.FindPassword
				.findPasswordSend(email)));
	}

	/**
	 * 找回密码发送邮件链接成功页面
	 * 
	 * @param email
	 * @return
	 */
	public Result findPasswordSend(String email) {
		return ok(views.html.member.findPassword.find_password_send
				.render(email));
	}

}
