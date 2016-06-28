package extensions.member.account.impl;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberShippingAddressMenuProvider implements
		IMemberAccountMenuProvider {

	public static final String ID = "shipping-address";

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
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.member.address.menu_shipping_address_book_setting
				.render(highliht);
	}

}
