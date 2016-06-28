package extensions.manager;

import javax.inject.Inject;

import play.mvc.Http.Context;
import session.ISessionService;
import entity.manager.AdminUser;
import extensions.livechat.role.SupportRoleProvider;

public class OperatorRoleProvider implements SupportRoleProvider {

	@Inject
	ISessionService sessionService;

	@Override
	public boolean isInRole(Context context) {
		AdminUser user = getAdminUser();
		if (user == null) {
			return false;
		}
		return true;
	}

	@Override
	public String getAlias(Context context) {
		AdminUser user = getAdminUser();
		if (user != null) {
			return user.getCusername();
		}
		return "N/A";
	}

	protected AdminUser getAdminUser() {
		AdminUser user = (AdminUser) sessionService.get("ADMIN_LOGIN_CONTEXT");
		return user;
	}
}
