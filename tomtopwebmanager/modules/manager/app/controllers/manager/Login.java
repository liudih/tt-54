package controllers.manager;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import play.Logger;
import play.data.Form;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import services.manager.CheckLoginUser;
import services.manager.LoginService;
import session.ISessionService;
import base.util.md5.MD5;

import com.google.inject.Inject;

import forms.manager.login.LoginForm;
import forms.manager.login.PublicLoginForm;

public class Login extends Controller {

	@Inject
	LoginService loginService;
	@Inject
	CheckLoginUser checkLoginUser;
	
	@Inject
	ISessionService sessionService;
	public Result loginForm() {
		Form<LoginForm> form = Form.form(LoginForm.class);
		return ok(views.html.manager.login.render(form));
	}

	public Result login() {
		Logger.debug("join login");
		Form<LoginForm> form = Form.form(LoginForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest(views.html.manager.login.render(form));
		}
		LoginForm login = form.get();

		String cpasspw = MD5.md5(login.getPassword());
		// 登录时转成小写
		login.setUsername(login.getUsername().toLowerCase());
		
		if (loginService.login(login.getUsername(), cpasspw)) {
			Logger.debug("Login Success: {}", login.getUsername());
			return redirect(controllers.manager.routes.Application.index());
		}
		form.reject("Login Error");
		return ok(views.html.manager.login.render(form));
	}
	
	public Result publicLogin(String jobNumber,String sysName,String tp,String signData,String sid) {
		Logger.debug("publicLogin");
		/*Form<PublicLoginForm> form = Form.form(PublicLoginForm.class).bindFromRequest();
		if (form.hasErrors()) {
			return badRequest();
		}*/
			
			PublicLoginForm publicLoginForm =new PublicLoginForm();// form.get();
			publicLoginForm.setJobNumber(jobNumber);
			publicLoginForm.setSignData(signData);
			publicLoginForm.setSysName(sysName);
			publicLoginForm.setTimestamp(tp);
			publicLoginForm.setSeId(sid);
			Logger.debug("publicLogin publicLoginForm:"+publicLoginForm);
			//安全验证
			
			Form<LoginForm> form = Form.form(LoginForm.class);
			Map<String, Object> validateLoginUser = checkLoginUser.validateLoginUser(publicLoginForm);
			if(MapUtils.isNotEmpty(validateLoginUser)){
				Logger.debug("Login  publicLogin fail!   publicLoginForm:{}"+publicLoginForm+"    validateLoginUser: {}", validateLoginUser);
				return ok(views.html.manager.login.render(form));
			}
			//需要回调management
			Promise<Map> callBack = checkLoginUser.callBack(signData, jobNumber, tp, sysName);
			Map map = callBack.get(6000);
			if(MapUtils.isEmpty(map) || "N".equals(map.get("status"))){//如果返回错误或者是失败
				Logger.debug("Login  publicLogin call back fail!   map:{}"+map);
				return ok(views.html.manager.login.render(form));
			}
			
			if (loginService.publicLogin(publicLoginForm.getJobNumber()) && "Y".equals(map.get("status")) ) {
				Logger.debug("Login Success: {}", publicLoginForm.getUsername());
				return redirect(controllers.manager.routes.Application.index());
			}
		 return ok(views.html.manager.login.render(form));
	}
}
