package extensions.member.account;

import play.twirl.api.Html;

public class MemberEditMenuProvider implements IMemberAccountMenuProvider {
	public static final String ID = "edit";

	@Override
	public String getCategory() {
		return "member";
	}

	@Override
	public int getDisplayOrder() {
		return 2;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht = ID.equals(currentMenuID);
		return views.html.member.account.member_edit_menu.render(highliht);
	}

}
