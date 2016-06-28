package extensions.member.account.impl;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import play.twirl.api.Html;
import services.base.FoundationService;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MyprofileMenuProvider implements IMemberAccountHomeFragmentProvider{

	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.BODY,Position.BODY_SIMPLE);
	}

	@Override
	public int getDisplayOrder() {
		return 70;
	}

	@Override
	public Html getFragment(String email, Position position) {
		if(this.getPosition().contains(position)){
			return views.html.member.quickMenus.myprofile_menu.render();
		}else{
			return null;
		}
	}

}
