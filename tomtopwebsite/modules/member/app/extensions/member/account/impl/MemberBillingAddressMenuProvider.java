package extensions.member.account.impl;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberBillingAddressMenuProvider implements
		IMemberAccountMenuProvider {

	public static final String ID = "billing-address";

	@Override
	public String getCategory() {
		return "account";
	}

	@Override
	public int getDisplayOrder() {
		return 21;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.member.address.menu_billing_address_book_setting
				.render(highliht);
	}

}
