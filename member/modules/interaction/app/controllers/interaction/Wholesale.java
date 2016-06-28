package controllers.interaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import play.data.Form;
import play.data.validation.ValidationError;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.captcha.CaptchaService;
import services.interaction.WholesaleService;
import forms.interaction.WholesaleInquiryForm;

public class Wholesale extends Controller {

	@Inject
	WholesaleService wholesaleService;
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
		
		play.data.Form<WholesaleInquiryForm> userForm = Form.form(WholesaleInquiryForm.class).bindFromRequest();
		if(userForm.hasErrors()){
			Iterator err = userForm.errors().entrySet().iterator();
			Map.Entry<String, List<ValidationError>> entry= (Entry<String, List<ValidationError>>) err.next();  
			mjson.put("result", entry.getKey()+":  "+entry.getValue().get(0).message());
			return ok(Json.toJson(mjson));
		}
		boolean flag = wholesaleService.addWholesaleInquiry(userForm.get());
		if(flag){
			mjson.put("result", "success");
		}
		return ok(Json.toJson(mjson));
	}

}
