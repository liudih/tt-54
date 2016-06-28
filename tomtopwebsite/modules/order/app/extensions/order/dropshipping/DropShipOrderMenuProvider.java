package extensions.order.dropshipping;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.dropship.DropShipBaseEnquiryService;
import services.member.login.LoginService;
import extensions.member.account.IMemberAccountMenuProvider;

public class DropShipOrderMenuProvider implements IMemberAccountMenuProvider {
	@Inject
	DropShipBaseEnquiryService dropShipBaseEnquiryService;
	@Inject
	LoginService loginService;
	@Inject
	FoundationService foundationService;

	public static final String ID = "dropship-order-list";

	@Override
	public String getCategory() {
		return "dropship";
	}

	@Override
	public int getDisplayOrder() {
		return 14;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		String email = loginService.getLoginEmail();
		int siteID = foundationService.getSiteID();
		boolean dropShipBase = dropShipBaseEnquiryService
				.checkoutDropShipBaseByEmail(email, siteID);
		if (dropShipBase) {
			final boolean highliht = ID.equals(currentMenuID);
			return views.html.order.member.dropship.dropship_order_menu
					.render(highliht);
		} else {
			return null;
		}
	}

}
