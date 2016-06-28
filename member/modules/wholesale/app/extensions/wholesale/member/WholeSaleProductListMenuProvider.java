package extensions.wholesale.member;

import javax.inject.Inject;

import authenticators.member.MemberLoginAuthenticator;
import play.Logger;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.member.login.ILoginService;
import services.wholesale.WholeSaleBaseEnquiryService;
import extensions.member.account.IMemberAccountMenuProvider;

public class WholeSaleProductListMenuProvider implements
		IMemberAccountMenuProvider {
	@Inject
	WholeSaleBaseEnquiryService wholeSaleBaseEnquiryService;
	@Inject
	ILoginService loginService;
	@Inject
	FoundationService foundationService;
	public static final String ID = "wholesalelist";

	@Override
	public String getCategory() {
		return "wholesale";
	}

	@Override
	public int getDisplayOrder() {
		return 16;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		String email = loginService.getLoginEmail();
		int siteID = foundationService.getSiteID();
		boolean isWholeSale = wholeSaleBaseEnquiryService
				.checkWholeSaleBaseByEmail(email, siteID);
		if (isWholeSale) {
			final boolean highliht = ID.equals(currentMenuID);
			return views.html.wholesale.member.wholesale_product_list_menu
					.render(highliht);
		} else {
			return Html.apply("");
		}
	}

}
