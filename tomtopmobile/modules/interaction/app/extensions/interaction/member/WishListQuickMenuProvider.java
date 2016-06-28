package extensions.interaction.member;

import java.util.List;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.interaction.ICollectService;
import valueobjects.base.LoginContext;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class WishListQuickMenuProvider implements
		IMemberAccountHomeFragmentProvider {

	@Inject
	ICollectService collectService;

	@Inject
	FoundationService foundation;

	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.BODY, Position.BODY_SIMPLE,
				Position.BODY_NOTLOGIN);
	}

	@Override
	public int getDisplayOrder() {
		return 40;
	}

	@Override
	public Html getFragment(String email, Position position) {

		if (this.getPosition().contains(position)) {
			List<String> wishList = collectService
					.getCollectListingIDByEmail(email);

			LoginContext lc = foundation.getLoginContext();

			return views.html.interaction.home.quickmenu.wishlist_quick_menu
					.render(wishList.size(), lc.isLogin());
		} else {
			return null;
		}

	}

}
