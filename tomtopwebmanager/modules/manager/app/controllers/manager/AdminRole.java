package controllers.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import com.google.api.client.util.Maps;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.manager.AdminMenuRoleEnquiryService;
import services.manager.AdminRoleService;
import session.ISessionService;
import entity.manager.AdminMenu;
import entity.manager.MenuRoleMap;
import forms.AdminRoleForm;

@controllers.AdminRole(menuName = "PermissionMgr")
public class AdminRole extends Controller {

	final static int NOI_ERROR = 0;

	final static int DELETE_ERROR = 1;

	@Inject
	AdminRoleService roleService;

	@Inject
	AdminMenuRoleEnquiryService menRoleService;

	@Inject
	ISessionService sessionService;

	public Result roleManager() {
		List<entity.manager.AdminRole> roleist = roleService.getAllAdminRole();

		return ok(views.html.manager.role.role_manager.render(roleist));
	}

	public Result addAdminRole() {
		Form<AdminRoleForm> form = Form.form(AdminRoleForm.class)
				.bindFromRequest();

		entity.manager.AdminRole role = new entity.manager.AdminRole();
		AdminRoleForm uform = form.get();
		BeanUtils.copyProperties(uform, role);

		entity.manager.AdminRole type = roleService.addAdminRole(role);
		return ok(Json.toJson(type));
	}

	public Result updateAdminRole() {

		Form<AdminRoleForm> form = Form.form(AdminRoleForm.class)
				.bindFromRequest();

		entity.manager.AdminRole role = new entity.manager.AdminRole();
		AdminRoleForm uform = form.get();
		BeanUtils.copyProperties(uform, role);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (roleService.updateAdminRole(role)) {
			resultMap.put("errorCode", NOI_ERROR);
			return ok(Json.toJson(resultMap));
		}
		Logger.debug("角色更新失败");
		resultMap.put("errorCode", DELETE_ERROR);
		return ok(Json.toJson(resultMap));
	}

	public Result deleteAdminRole(Long iid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 删除角色前需要验证此角色下是否有关联用户，如果有则不允许删除
		if(roleService.validateExistUser(iid)){
			if (roleService.deleteAdminRole(iid)) {
				resultMap.put("errorCode", NOI_ERROR);
				resultMap.put("msg", "删除成功");
				return ok(Json.toJson(resultMap));
			}else{
				resultMap.put("errorCode", DELETE_ERROR);
				resultMap.put("msg", "删除失败");
				return ok(Json.toJson(resultMap));
			}
		}else{
			resultMap.put("errorCode", DELETE_ERROR);
			resultMap.put("msg", "角色有关联用户， 不允许删除");
			return ok(Json.toJson(resultMap));
		}
	}

	public Result roleFunctionManager() {

		return ok(views.html.manager.role.menu_role_settings.render());
	}

	public Result addMenuRole(Integer roleId, String chklist) {
		String[] chkArr = chklist.split(",");
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");
		this.menRoleService.deleteMenuRoleByRoleId(roleId);

		Integer result = 0;
		for (String menuId : chkArr) {

			MenuRoleMap menuRole = new MenuRoleMap();
			menuRole.setImenuid(Integer.parseInt(menuId));
			menuRole.setIroleid(roleId);
			menuRole.setCcreateuser(user.getCusername());

			result += this.menRoleService.insert(menuRole);
		}

		Map<String, Object> resultMap = Maps.newHashMap();
		if (result > 0) {
			resultMap.put("errorCode", 0);
		} else {
			resultMap.put("errorCode", 1);
		}
		return ok(Json.toJson(resultMap));
	}

	public Result getMenuByRoleId(Integer roleId) {

		List<Integer> menuIds = this.menRoleService.getMenuIdsByRoleId(roleId);

		return ok(Json.toJson(menuIds));
	}

}
