package extensions.livechat.role;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;

import play.mvc.Http.Context;
import services.base.FoundationService;

public class GuestEnquiryRoleProvider implements EnquiryRoleProvider {

	@Inject
	FoundationService foundation;

	@Override
	public boolean isInRole(Context context) {
		if (foundation.getLoginContext().isLogin()) {
			return false;
		}
		return true;
	}

	@Override
	public String getAlias(Context context) {
		return "guest-" + RandomStringUtils.random(4, "0123456789");
	}

}
