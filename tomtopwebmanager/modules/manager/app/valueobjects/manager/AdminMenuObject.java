package valueobjects.manager;

import java.util.List;

import mapper.manager.AdminMenuMapper;

import com.google.api.client.util.Lists;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;

public class AdminMenuObject implements ImanagerValueObject {
	final List<entity.manager.AdminMenu> adminMenus;

	@Inject
	AdminMenuMapper adminMenuMapper;

	public AdminMenuObject(List<entity.manager.AdminMenu> adminMenus) {
		this.adminMenus = adminMenus;

	}

	public List<entity.manager.AdminMenu> getAdminTopMenus() {
		return Lists.newArrayList(Collections2.filter(adminMenus, a -> a
				.getIlevel().equals(1)));
	}

	public List<entity.manager.AdminMenu> getAdminNextMenus(int parentid) {
		return Lists.newArrayList(Collections2.filter(adminMenus, a -> a
				.getIlevel().equals(2) && a.getIparentid().equals(parentid)));
	}

}
