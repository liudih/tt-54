package extensions.interaction.member;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountMenuProvider;

public class PriceMatchMenuProvider implements IMemberAccountMenuProvider {

	public static final String ID = "price-match";

	@Override
	public String getCategory() {
		return "shopping";
	}

	@Override
	public int getDisplayOrder() {
		return 40;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		final boolean highliht=ID.equals(currentMenuID);
		return views.html.interaction.member.menu_priceMatch.render(highliht);
	}

}
