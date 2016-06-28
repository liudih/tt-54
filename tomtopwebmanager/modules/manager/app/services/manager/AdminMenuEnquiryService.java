package services.manager;

import java.util.List;

import mapper.manager.AdminMenuMapper;
import mapper.manager.AdminMenuRoleMapper;
import session.ISessionService;
import valueobjects.manager.AdminMenuObject;

import com.google.inject.Inject;

import entity.manager.AdminMenu;
import entity.manager.AdminUser;
import extensions.InjectorInstance;

public class AdminMenuEnquiryService {
	final static String ADMIN_SESSION_NAME = "ADMIN_LOGIN_CONTEXT";
	@Inject
	AdminMenuMapper adminMenuMapper;

	@Inject
	ISessionService sessionService;
	
	@Inject
	AdminMenuRoleMapper menuRoleMapper;

	public static AdminMenuEnquiryService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				AdminMenuEnquiryService.class);
	}

	/**
	 * 此方法是根据用户登录后查询出 配置的相菜单
	 * @return
	 */
	public AdminMenuObject getAdminMenuObject() {
		AdminUser user = (AdminUser) sessionService.get(ADMIN_SESSION_NAME);
//		List<entity.manager.AdminMenu> adminMenu;
		
		/**
		 * 1.根据登录用户找到角色
		 * 2.根据角色找到 配置的菜单项ID
		 * 3.根据菜单项ID找到菜单
		 */
		List<Integer> roleIds = menuRoleMapper.getRoleIdByUserId(user.getIid());
		
		List<Integer> menuIds = menuRoleMapper.getMenuIdsByRoleIds(roleIds);
		
		List<AdminMenu> menuList = adminMenuMapper.getAdminMenuByMenuIds(menuIds);
//		if (user.isBadmin()) {
//			adminMenu = adminMenuMapper.getAllMenu();
//		} else {
//			adminMenu = adminMenuMapper.getMenuForUserId(user.getIid());
//		}

		return new AdminMenuObject(menuList);
	}
	
	
	/**
	 * 角色权限配置的时候显示所有
	 * @return
	 */
	public AdminMenuObject getAllMenuObject() {
		
		List<AdminMenu> menuList = adminMenuMapper.getAllMenu();
		
		return new AdminMenuObject(menuList);
	}

}
