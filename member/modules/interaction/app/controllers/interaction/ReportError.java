package controllers.interaction;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.captcha.CaptchaService;
import services.interaction.ReportErrorService;
import forms.interaction.ReportErrorForm;

public class ReportError extends Controller {

	@Inject
	ReportErrorService reportErrorService;
	@Inject
	CaptchaService captchaService;
	
	public Result addData() {
		Map<String,Object> mjson = new HashMap<String,Object>();
		mjson.put("result", "error");
		String captcha = request().body().asFormUrlEncoded().get("captcha")[0];
		if(!captchaService.verify(captcha)){
			mjson.put("result", "wrongcaptcha");
			return ok(Json.toJson(mjson)); 
		}
		
		play.data.Form<ReportErrorForm> userForm = Form.form(ReportErrorForm.class).bindFromRequest();
		if(userForm.hasErrors()){
			mjson.put("result", "Data cannot be empty!");
			return ok(Json.toJson(mjson));
		}
		boolean flag = reportErrorService.addReportError(userForm.get());
		if(flag){
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}
}
