package extensions.interaction.livechat;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.base.FoundationService;
import services.member.login.LoginService;
import extensions.livechat.role.EnquiryRoleProvider;

public class MemberEnquiryRoleProvider implements EnquiryRoleProvider {

	@Inject
	FoundationService foundation;

	@Inject
	LoginService login;

	@Override
	public boolean isInRole(Context context) {
		if (foundation.getLoginContext().isLogin()) {
			return true;
		}
		return false;
	}

	@Override
	public String getAlias(Context context) {
		return login.getLoginEmail();
	}

}
