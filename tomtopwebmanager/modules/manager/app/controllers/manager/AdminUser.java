package controllers.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import services.base.WebsiteService;
import services.manager.AdminMenuRoleEnquiryService;
import services.manager.AdminUserService;
import services.manager.AdminUserWebsiteMapService;
import services.manager.LoginService;
import services.manager.UserRoleMapService;
import session.ISessionService;
import valueobjects.base.Page;
import base.util.md5.MD5;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimaps;

import controllers.InterceptActon;
import dto.Website;
import entity.manager.AdminUserWebsitMap;
import entity.manager.UserRoleMap;
import extensions.livechat.CustomerServiceFilter;
import forms.AdminUserForm;

@controllers.AdminRole(menuName = "UserMgr")
@With(InterceptActon.class)
public class AdminUser extends Controller {

	final static int NOI_ERROR = 0;

	final static int DELETE_ERROR = 1;

	@Inject
	AdminUserService adminUserService;

	@Inject
	UserRoleMapService userRoleMapService;

	@Inject
	ISessionService sessionService;

	@Inject
	AdminMenuRoleEnquiryService enquiryService;

	@Inject
	LoginService loginService;

	@Inject
	WebsiteService websiteEnquiryService;

	@Inject
	AdminUserWebsiteMapService adminUserWebsiteMapService;

	@Inject
	CustomerServiceFilter customerServiceFilter;

	public Result userList(int page) {
		Page<dto.AdminUser> p = adminUserService.getadminUserPage(page);
		List<Website> websites = websiteEnquiryService.getAll();

		Multimaps.transformValues(
				FluentIterable.from(websites).index(c -> c.getIid()),
				self -> self.getCcode());

		return ok(views.html.manager.user.userlist.render(p, "","", -1, websites));
	}

	public Result deleteUser(int iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (adminUserService.deleteAdminUser(iid)) {
			adminUserWebsiteMapService.deleteByUserId(iid);
			resultMap.put("errorCode", NOI_ERROR);
			resultMap.put("iid", iid);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));

	}

	public Result addUser() {
		Form<AdminUserForm> form = Form.form(AdminUserForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			return ok(views.html.manager.user.error.render());
		}
		entity.manager.AdminUser adminUser = new entity.manager.AdminUser();
		AdminUserForm uform = form.get();
		BeanUtils.copyProperties(uform, adminUser);
		String cpasswd = MD5.md5(adminUser.getCpasswd());
		adminUser.setCpasswd(cpasswd);
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		adminUser.setCcreateuser(user.getCusername());
		// 存储时转成小写
		adminUser.setCusername(adminUser.getCusername().toLowerCase());
		if (adminUserService.addAdminUser(adminUser)) {

			UserRoleMap userRole = new UserRoleMap();
			Map<String, String[]> map = request().body().asFormUrlEncoded();
			String[] checkedVal = map.get("iadminroleid");
			for (String roleId : checkedVal) {

				userRole.setIroleid(Integer.parseInt(roleId));
				userRole.setIuserid(adminUser.getIid());
				userRole.setCcreateuser(user.getCusername());

				this.userRoleMapService.addUserRole(userRole);
			}

			String[] websiteIds = map.get("iwebsiteids");
			List<AdminUserWebsitMap> adminUserWebsitMaps = new ArrayList<AdminUserWebsitMap>();
			for (String websiteId : websiteIds) {
				AdminUserWebsitMap websitMap = new AdminUserWebsitMap();
				websitMap.setIuserid(adminUser.getIid());
				websitMap.setIwebsiteid(Integer.parseInt(websiteId));
				websitMap.setCcreateuser(user.getCcreateuser());
				websitMap.setDcreatedate(new Date());
				adminUserWebsitMaps.add(websitMap);
			}
			if (adminUserWebsitMaps.size() > 0) {
				adminUserWebsiteMapService.insertBatch(adminUserWebsitMaps);
			}

			return redirect(controllers.manager.routes.AdminUser.userList(1));
		}
		return ok(views.html.manager.user.error.render());
	}

	public Result editForm(int iid) {
		dto.AdminUser adminUser = adminUserService.getAdminUser(iid);

		List<Integer> roleIds = enquiryService.getRoleIdByUserId(adminUser
				.getIid());

		List<Integer> websiteIds = adminUserWebsiteMapService
				.getAdminUserWebsitMapsByUserId(adminUser.getIid());

		List<Website> websites = websiteEnquiryService.getAll();
		if (adminUser != null) {
			return ok(views.html.manager.user.user_edit.render(adminUser,
					roleIds, websites, websiteIds));
		}
		return notFound();

	}

	public Result editUser() {
		Form<AdminUserForm> form = Form.form(AdminUserForm.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			flash().put("error", Json.toJson(form.errorsAsJson()).toString());

		}
		entity.manager.AdminUser adminUser = new entity.manager.AdminUser();
		AdminUserForm uform = form.get();
		BeanUtils.copyProperties(uform, adminUser);
		// 存储时转成小写
		adminUser.setCusername(adminUser.getCusername().toLowerCase());
		Logger.debug("adminUser:{}", adminUser);
		if (adminUserService.updateAdminUser(adminUser)) {
			UserRoleMap userRole = new UserRoleMap();
			entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
					.get("ADMIN_LOGIN_CONTEXT");
			Map<String, String[]> map = request().body().asFormUrlEncoded();
			String[] checkedVal = map.get("iadminroleid");
			this.userRoleMapService.deleteUserRoleByUserId(adminUser.getIid());
			for (String roleId : checkedVal) {

				userRole.setIroleid(Integer.parseInt(roleId));
				userRole.setIuserid(adminUser.getIid());
				userRole.setCcreateuser(user.getCusername());

				this.userRoleMapService.addUserRole(userRole);
			}
			adminUserWebsiteMapService.deleteByUserId(adminUser.getIid());
			String[] websiteIds = map.get("iwebsiteids");
			List<AdminUserWebsitMap> adminUserWebsitMaps = new ArrayList<AdminUserWebsitMap>();
			for (String websiteId : websiteIds) {
				AdminUserWebsitMap websitMap = new AdminUserWebsitMap();
				websitMap.setIuserid(adminUser.getIid());
				websitMap.setIwebsiteid(Integer.parseInt(websiteId));
				websitMap.setCcreateuser(user.getCcreateuser());
				websitMap.setDcreatedate(new Date());
				adminUserWebsitMaps.add(websitMap);
			}
			if (adminUserWebsitMaps.size() > 0) {
				adminUserWebsiteMapService.insertBatch(adminUserWebsitMaps);
			}
		} else {
			flash().put("error", "server error");
		}

		return redirect(controllers.manager.routes.AdminUser.userList(1));
	}

	public Result resetPwdForm(int iid) {
		dto.AdminUser adminUser = adminUserService.getAdminUser(iid);

		if (adminUser != null) {
			return ok(views.html.manager.user.resetpwd.render(adminUser));
		}
		return notFound();

	}

	public Result resetPwd() {
		DynamicForm df = Form.form().bindFromRequest();
		String iid = df.get("iid");

		String newPwdOne = df.get("cnewpasswd_one");
		String newPwdTwo = df.get("cnewpasswd_two");

		// 验证密码一致性
		if (newPwdOne.equals(newPwdTwo)) {
			Logger.info("重置密码");
			entity.manager.AdminUser adminUser = this.adminUserService
					.selectByPrimaryKey(Integer.parseInt(iid));
			String cpasswd = MD5.md5(newPwdOne);
			adminUser.setCpasswd(cpasswd);
			this.adminUserService.updateAdminUser(adminUser);
		} else {
			Logger.info("重置密码两次输入不一致，请重新输入！");
		}

		return redirect(controllers.manager.routes.AdminUser.userList(1));
	}

	public Result modifyPwdList() {

		return ok(views.html.manager.user.modifypwd.render());
	}

	public Result checkOldPwd(String oldPwd) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");

		String cpasspw = MD5.md5(oldPwd);

		if (loginService.login(user.getCusername(), cpasspw)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result modifyPwd(String oldPwd, String newPwdOne, String newPwdTwo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");

		String cpasspw = MD5.md5(oldPwd);

		if (!loginService.login(user.getCusername(), cpasspw)) {
			resultMap.put("errorCode", DELETE_ERROR);
			resultMap.put("msg", "旧密码输入错误，请重新输入！");
			return ok(Json.toJson(resultMap));
		}

		if (!newPwdOne.equals(newPwdTwo)) {
			resultMap.put("errorCode", DELETE_ERROR);
			resultMap.put("msg", "两次密码输入不一致，请重新输入！");
			return ok(Json.toJson(resultMap));
		}

		Logger.info("修改密码:" + user.getCusername());
		String cpasswd = MD5.md5(newPwdOne);
		user.setCpasswd(cpasswd);
		this.adminUserService.updateAdminUser(user);

		resultMap.put("errorCode", NOI_ERROR);
		resultMap.put("msg", "密码修改成功！");
		return ok(Json.toJson(resultMap));
	}

	public Result exit() {

		sessionService.remove("ADMIN_LOGIN_CONTEXT");

		return redirect(controllers.manager.routes.Application.index());
	}

	public Result search() {
		DynamicForm in = Form.form().bindFromRequest();
		String cusername = in.get("cusername_search");
		cusername = cusername.toLowerCase();
		
		String cjobnumber = in.get("cjobnumber_search");
		cjobnumber = cjobnumber.toLowerCase();
		
		String iroleidStr = in.get("iadminroleid_search");
		Integer iroleid = null != iroleidStr && !"".equals(iroleidStr) ? Integer
				.parseInt(iroleidStr) : null;
		List<Website> websites = websiteEnquiryService.getAll();

		Multimaps.transformValues(
				FluentIterable.from(websites).index(c -> c.getIid()),
				self -> self.getCcode());
		Page<dto.AdminUser> p = this.adminUserService.searchAdminUserPage(1,
				cusername, cjobnumber, iroleid);

		return ok(views.html.manager.user.userlist.render(p, cusername, cjobnumber,
				iroleid, websites));
	}

	public Result generatePassword(Integer length) {
		String pwd = this.adminUserService.generatePassword(length);
		return ok(Json.toJson(pwd));
	}

	public Result getLiveChatUsers(int professionId, int langageId) {
		List<Integer> userids = customerServiceFilter.getUsers(professionId,
				langageId);
		if (null != userids) {
			return ok(Json.toJson(userids));
		} else {
			return ok();
		}
	}

	public Result getLiveChatOptimalUsers(int professionId, int langageId) {
		String ltc = customerServiceFilter.getOptimalUser(professionId,
				langageId);
		if (null != ltc)
			return ok(Json.toJson(ltc));
		else
			return ok();
	}

	public Result validateJobNumber(String jobnumber){
		//先验证工号是否已经添加
		boolean flag = adminUserService.validateJobNumber(jobnumber);
		
		return ok(Json.toJson(flag));
	}
	
	public Result validateUserName(String username){
		//先验证工号是否已经添加
		boolean flag = adminUserService.validateUserName(username);
		
		return ok(Json.toJson(flag));
	}
}
