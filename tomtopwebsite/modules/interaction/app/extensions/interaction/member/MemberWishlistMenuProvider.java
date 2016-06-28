package extensions.interaction.member;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class MemberWishlistMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID = "wishlist";

	@Override
	public String getCategory() {
		return "shopping";
	}

	@Override
	public int getDisplayOrder() {
		return 30;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht=ID.equals(currentMenuID);
		return views.html.interaction.member.menu_wishlist.render(highliht);
	}

}
