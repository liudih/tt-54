package extensions.wholesale.member;

import javax.inject.Inject;

import authenticators.member.MemberLoginAuthenticator;
import play.mvc.Security.Authenticated;
import play.twirl.api.Html;
import services.base.FoundationService;
import services.member.login.ILoginService;
import services.wholesale.WholeSaleBaseEnquiryService;
import extensions.member.account.IMemberAccountMenuProvider;

public class WholeSaleMenuProvider implements IMemberAccountMenuProvider {
	@Inject
	WholeSaleBaseEnquiryService wholeSaleBaseEnquiryService;
	@Inject
	ILoginService loginService;
	@Inject
	FoundationService foundationService;
	public static final String ID = "joinwholesale";

	@Override
	public String getCategory() {
		return "wholesale";
	}

	@Override
	public int getDisplayOrder() {
		return 15;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		String email = loginService.getLoginEmail();
		int siteID = foundationService.getSiteID();
		boolean isWholeSale = wholeSaleBaseEnquiryService
				.checkWholeSaleBaseByEmail(email, siteID);
		if (isWholeSale) {
			return Html.apply("");
		} else {
			final boolean highliht = ID.equals(currentMenuID);
			return views.html.wholesale.member.wholesale_menu.render(highliht);
		}
	}

}
