package extensions.interaction.member;

import play.twirl.api.Html;
import extensions.member.account.IMemberQuickMenuProvider;

public class WishListQuickMenuProvider implements IMemberQuickMenuProvider {

	@Override
	public int getDisplayOrder() {
		return 40;
	}

	@Override
	public Html getQuickMenuItem() {
		return views.html.interaction.collect.quick_menu.render();
	}

}
