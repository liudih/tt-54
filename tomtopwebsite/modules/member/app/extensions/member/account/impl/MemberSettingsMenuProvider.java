package extensions.member.account.impl;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.member.IMemberEnquiryService;
import dto.member.MemberBase;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberSettingsMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID = "profile-update";

	@Inject
	FoundationService foundation;

	@Inject
	IMemberEnquiryService enquiry;

	@Override
	public String getCategory() {
		return "account";
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		String memberID = foundation.getLoginContext().getMemberID();
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.member.menu_account_settings.render(highliht);
	}

}
