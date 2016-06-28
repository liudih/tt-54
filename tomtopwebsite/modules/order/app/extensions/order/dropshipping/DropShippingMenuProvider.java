package extensions.order.dropshipping;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.dropship.DropShipBaseEnquiryService;
import services.member.login.LoginService;
import extensions.member.account.IMemberAccountMenuProvider;

public class DropShippingMenuProvider implements IMemberAccountMenuProvider {
	public static final String ID = "dropShipping-uploadOrder";

	@Inject
	private DropShipBaseEnquiryService dropShipBaseEnquiry;
	@Inject
	private LoginService loginService;
	@Inject
	private FoundationService foundtion;

	@Override
	public String getCategory() {
		return "dropship";
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		String email = loginService.getLoginEmail();
		int siteID = foundtion.getSiteID();
		boolean dropShipBase = dropShipBaseEnquiry.checkoutDropShipBaseByEmail(
				email, siteID);
		if (dropShipBase) {
			final boolean highliht = ID.equals(currentMenuID);
			return views.html.order.dropShipping.menu.render(highliht);
		} else {
			return Html.apply("");
		}
	}

}
