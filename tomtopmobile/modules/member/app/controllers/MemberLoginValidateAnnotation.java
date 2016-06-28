package controllers;

import play.Logger;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.base.FoundationService;
import valueobjects.base.LoginContext;

import com.google.inject.Inject;

public class MemberLoginValidateAnnotation extends Action<MemberLoginValidate> {

	@Inject
	FoundationService foundation;

	public F.Promise<Result> call(Http.Context ctx) throws Throwable {
		LoginContext loginContext = foundation.getLoginContext();
		if(null == loginContext){
			Logger.debug("权限验证不通过，不进入方法，直接跳入登录界面");
			return F.Promise.pure(redirect(controllers.member.routes.Login
					.login(0)));
		}
		boolean flag = loginContext.isLogin();
		if (flag) {
			Logger.info("权限验证通过，进入相应方法");
			return delegate.call(ctx);
		} else {
			Logger.debug("权限验证不通过，不进入方法，直接跳入登录界面");
			return F.Promise.pure(redirect(controllers.member.routes.Login
					.login(0)));
		}
	}
}
