package services.manager;

import java.util.List;

import mapper.manager.AdminMenuMapper;
import mapper.manager.AdminMenuRoleMapper;
import session.ISessionService;

import com.google.inject.Inject;

import entity.manager.AdminMenu;
import entity.manager.MenuRoleMap;
import extensions.InjectorInstance;

public class AdminMenuRoleEnquiryService {

	@Inject
	AdminMenuRoleMapper adminMenuRoleMapper;

	@Inject
	AdminMenuMapper adminMenuMapper;
	
	@Inject
	ISessionService sessionService;

	public static AdminMenuRoleEnquiryService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				AdminMenuRoleEnquiryService.class);
	}

	public boolean validate(Integer roleid, Integer imenuid) {
		int result = adminMenuRoleMapper.validate(roleid, imenuid);

		return result > 0 ? true : false;
	}

	public List<Integer> getRoleIdByUserId(Integer userId) {
		return adminMenuRoleMapper.getRoleIdByUserId(userId);
	}

	public Integer getMenuIdByMenuName(String menuName) {
		return adminMenuRoleMapper.getMenuIdByMenuName(menuName);
	}

	public int deleteByPrimaryKey(Long iid) {
		return adminMenuRoleMapper.deleteByPrimaryKey(iid);
	}

	public int deleteMenuRoleByRoleId(Integer iroleid) {
		return adminMenuRoleMapper.deleteMenuRoleByRoleId(iroleid);
	}

	public int insert(MenuRoleMap record) {
		return adminMenuRoleMapper.insert(record);
	}

	public MenuRoleMap selectByPrimaryKey(Long iid) {
		return adminMenuRoleMapper.selectByPrimaryKey(iid);
	}

	public List<MenuRoleMap> getAllMenuRoleMapMap() {
		return adminMenuRoleMapper.getAllMenuRoleMapMap();
	}

	public int updateByPrimaryKey(MenuRoleMap record) {
		return adminMenuRoleMapper.updateByPrimaryKey(record);
	}
	
	public List<Integer> getMenuIdsByRoleId(Integer roleId) {
		return adminMenuRoleMapper.getMenuIdsByRoleId(roleId);
	}
	
	public List<AdminMenu> getAllMenu(){
		return this.adminMenuMapper.getAllMenu();
	}

}
