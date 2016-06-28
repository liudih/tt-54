package extensions.interaction.member;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.dropship.DropShipBaseEnquiryService;
import services.member.login.ILoginService;
import extensions.member.account.IMemberAccountMenuProvider;

public class DropshipProductBatchAddMenuProvider implements
		IMemberAccountMenuProvider {

	@Inject
	DropShipBaseEnquiryService dropShipBaseEnquiryService;

	@Inject
	ILoginService loginService;

	@Inject
	FoundationService foundationService;

	public static final String ID = "batch-add-dropship-product";

	@Override
	public String getCategory() {
		return "dropship";
	}

	@Override
	public int getDisplayOrder() {
		return 11;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht = ID.equals(currentMenuID);
		String email = loginService.getLoginEmail();
		int siteID = foundationService.getSiteID();
		boolean dropShipBase = dropShipBaseEnquiryService
				.checkoutDropShipBaseByEmail(email, siteID);
		if (dropShipBase) {
			return views.html.interaction.member.menu_dropship_product_batch_add
					.render(highliht);
		}
		return Html.apply("");
	}
}
