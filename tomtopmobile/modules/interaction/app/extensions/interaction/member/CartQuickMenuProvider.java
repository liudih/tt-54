package extensions.interaction.member;

import java.util.List;

import com.google.common.collect.Lists;

import play.twirl.api.Html;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;


public class CartQuickMenuProvider implements IMemberAccountHomeFragmentProvider {
	
	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.BODY, Position.BODY_SIMPLE, Position.BODY_NOTLOGIN);
	}

	@Override
	public int getDisplayOrder() {
		return 30;
	}

	@Override
	public Html getFragment(String email, Position position) {
		if(this.getPosition().contains(position)){
			return views.html.interaction.home.quickmenu.cart_quick_menu.render();
		}else{
			return null;
		}
	}
}
