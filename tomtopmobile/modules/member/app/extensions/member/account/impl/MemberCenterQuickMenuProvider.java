package extensions.member.account.impl;

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
public class MemberCenterQuickMenuProvider implements IMemberAccountHomeFragmentProvider {

	@Inject
	FoundationService foundation;
	
	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.BODY_SIMPLE, Position.BODY_NOTLOGIN);
	}

	@Override
	public int getDisplayOrder() {
		return 40;
	}

	@Override
	public Html getFragment(String email, Position position) {
		if(this.getPosition().contains(position)){
			boolean isLogin = foundation.getLoginContext().isLogin();
			return views.html.member.quickMenus.memeber_center.render(isLogin);
		}else{
			return null;
		}
	}
		
}
