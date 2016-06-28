package services.manager;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.AdminRoleMapper;
import entity.manager.AdminRole;
import extensions.InjectorInstance;

public class AdminRoleService {

	@Inject
	AdminRoleMapper mapper;

	public static AdminRoleService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				AdminRoleService.class);
	}

	public List<AdminRole> getAllAdminRole() {
		List<AdminRole> list = mapper.getAllAdminRole();

		return list;
	}

	public boolean deleteAdminRole(Long iid) {
		int result = mapper.deleteByPrimaryKey(iid);
		return result > 0 ? true : false;
	}

	public AdminRole addAdminRole(AdminRole aminRole) {
		mapper.insert(aminRole);
		return aminRole;
	}

	public boolean updateAdminRole(AdminRole aminRole) {
		int result = mapper.updateByPrimaryKey(aminRole);
		return result > 0 ? true : false;
	}

	public boolean validateExistUser(Long iid) {
		int result = mapper.validateExistUser(iid);
		return result == 0 ? true : false;
	}

	public AdminRole getAdminRoleByAdminId(int iid) {
		return mapper.getAdminRoleByAdminId(iid);
	}

}
