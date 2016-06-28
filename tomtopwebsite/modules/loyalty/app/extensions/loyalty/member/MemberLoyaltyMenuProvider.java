package extensions.loyalty.member;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberLoyaltyMenuProvider implements IMemberAccountMenuProvider {

	@Override
	public String getCategory() {
		return "account";
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		return views.html.loyalty.member.menu.render();
	}

}
