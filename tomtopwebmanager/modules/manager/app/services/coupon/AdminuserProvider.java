package services.coupon;

import javax.inject.Inject;

import services.loyalty.coupon.IAdminuserProvider;
import services.manager.AdminUserService;

public class AdminuserProvider implements IAdminuserProvider {
	@Inject
	AdminUserService adminUserService;

	@Override
	public Integer getCurrentUser() {
		if (null != adminUserService.getCuerrentUser()) {
			return adminUserService.getCuerrentUser().getIid();
		}
		return null;
	}

}
