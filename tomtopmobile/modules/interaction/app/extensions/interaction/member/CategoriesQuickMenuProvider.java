package extensions.interaction.member;

import java.util.List;

import play.twirl.api.Html;
import services.base.FoundationService;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

/**
 * Categories
 * 
 * @author lijun
 *
 */
public class CategoriesQuickMenuProvider implements IMemberAccountHomeFragmentProvider {

	@Inject
	FoundationService foundation;
	
	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.BODY_SIMPLE, Position.BODY_NOTLOGIN);
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getFragment(String email, Position position) {
		
		if(this.getPosition().contains(position)){
			boolean isLogin = foundation.getLoginContext().isLogin();
			return views.html.interaction.home.quickmenu.categories.render(isLogin);
		}else{
			return null;
		}
		
	}

		
}
