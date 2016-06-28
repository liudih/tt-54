package extensions.order.member;

import play.twirl.api.Html;
import extensions.member.account.IMemberQuickMenuProvider;

public class MemberOrderQuickMenuProvider implements IMemberQuickMenuProvider {

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getQuickMenuItem() {
		return views.html.order.member.quick_menu.render();
	}

}
