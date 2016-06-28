package controllers;

import java.util.List;

import play.Logger;
import play.data.Form;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import services.manager.AdminMenuRoleEnquiryService;
import session.ISessionService;

import com.google.inject.Inject;

import entity.manager.AdminUser;
import forms.manager.login.LoginForm;

public class AdminRoleAnnotationAction extends Action<AdminRole> {

	@Inject
	ISessionService sessionService;

	@Inject
	AdminMenuRoleEnquiryService enquiryService;

	final static String ADMIN_SESSION_NAME = "ADMIN_LOGIN_CONTEXT";

	public F.Promise<Result> call(Http.Context ctx) throws Throwable {

		if (sessionService.get(ADMIN_SESSION_NAME) == null) {

			Logger.info("未登录，不进入方法，直接跳入登录界面");
			Form<LoginForm> form = Form.form(LoginForm.class);
			return F.Promise.pure(ok(views.html.manager.login.render(form)));
		}

		// 首先判断是否已经登录， 如果登录了，再判断权限，否则直接跳到登录界面
		AdminUser user = (AdminUser) sessionService.get(ADMIN_SESSION_NAME);

		// 获取角色
		// 此处需要连接数据库查询操作
		List<Integer> roleIds = enquiryService.getRoleIdByUserId(user.getIid());
		String menuName = configuration.menuName();
		// 根据唯一名称获取menuid
		Integer menuId = enquiryService.getMenuIdByMenuName(menuName);
		
		boolean flag = false;
		if(null != roleIds && roleIds.size()> 0)
		{
			for(Integer roleId : roleIds){
				flag = enquiryService.validate(roleId, menuId);
				if(flag){
					break;
				}
			}
		}

		if (flag) {
			Logger.info("权限验证通过，进入相应方法");
			return delegate.call(ctx);
		} else {
			Logger.debug("权限验证不通过，不进入方法，直接跳入登录界面");
			Form<LoginForm> form = Form.form(LoginForm.class);
			return F.Promise.pure(ok(views.html.manager.login.render(form)));
		}
	}
}
