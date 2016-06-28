package extensions.member.account;

import play.twirl.api.Html;

public class MemberHomeMenuProvider implements IMemberAccountMenuProvider {
	public static final String ID = "home";

	@Override
	public String getCategory() {
		return "member";
	}

	@Override
	public int getDisplayOrder() {
		return 1;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.member.home.home_menu.render(highliht);
	}

}
