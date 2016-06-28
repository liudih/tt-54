package extensions.member.dropship;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.dropship.DropShipBaseEnquiryService;
import services.member.login.ILoginService;
import extensions.member.account.IMemberAccountMenuProvider;

public class DropShipMenuProvider implements IMemberAccountMenuProvider {
	@Inject
	DropShipBaseEnquiryService dropShipBaseEnquiryService;
	@Inject
	ILoginService loginService;
	@Inject
	FoundationService foundationService;
	public static final String ID = "dropshipjoin";

	@Override
	public String getCategory() {
		return "dropship";
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		String email = loginService.getLoginEmail();
		int siteID = foundationService.getSiteID();
		boolean dropShipBase = dropShipBaseEnquiryService
				.checkoutDropShipBaseByEmail(email, siteID);
		if (dropShipBase) {
			return Html.apply("");
		} else {
			final boolean highliht = ID.equals(currentMenuID);
			return views.html.member.dropship.dropship_menu.render(highliht);
		}
	}

}
