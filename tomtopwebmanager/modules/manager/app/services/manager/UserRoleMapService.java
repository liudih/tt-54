package services.manager;

import javax.inject.Inject;

import mapper.manager.UserRoleMapMapper;
import entity.manager.UserRoleMap;
import extensions.InjectorInstance;

public class UserRoleMapService {

	@Inject
	UserRoleMapMapper mapper;

	public static UserRoleMapService getInstance() {
		return InjectorInstance.getInjector().getInstance(
				UserRoleMapService.class);
	}

	public UserRoleMap addUserRole(UserRoleMap userRole) {
		mapper.insert(userRole);
		return userRole;
	}

	public Integer deleteUserRoleByUserId(Integer iuserid) {
		return mapper.deleteUserRoleByUserId(iuserid);
	}

}
