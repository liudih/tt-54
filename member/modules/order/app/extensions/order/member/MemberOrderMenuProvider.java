package extensions.order.member;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberOrderMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID = "order-list";
	
	@Override
	public String getCategory() {
		return "order";
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht=ID.equals(currentMenuID);
		return views.html.order.member.menu.render(highliht);
	}

}
