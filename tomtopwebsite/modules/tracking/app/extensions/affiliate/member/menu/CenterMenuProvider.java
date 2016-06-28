package extensions.affiliate.member.menu;

import javax.inject.Inject;

import play.twirl.api.Html;
import service.tracking.IAffiliateService;
import services.member.login.LoginService;
import extensions.member.account.IMemberAccountMenuProvider;

public class CenterMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID = "affiliate-center";

	@Override
	public String getCategory() {

		return "affiliate";
	}

	@Override
	public int getDisplayOrder() {

		return 2;
	}

	@Inject
	LoginService loginService;

	@Inject
	IAffiliateService affiliateService;

	@Override
	public Html getMenuItem(String currentMenuID) {
		String email = loginService.getLoginEmail();
		String aid = affiliateService.getAidByEmail(email);
		final boolean display = aid == null ? false : true;
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.affiliate.menu.center.render(display, highliht);
	}

}
