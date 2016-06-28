package extensions.interaction.member;

import play.twirl.api.Html;
import extensions.member.account.IMemberQuickMenuProvider;

public class ReviewsQuickMenuProvider implements IMemberQuickMenuProvider {

	@Override
	public int getDisplayOrder() {
		return 50;
	}

	@Override
	public Html getQuickMenuItem() {
		return views.html.interaction.review.quick_menu.render();
	}

}
