package extensions.member.account.impl;

import java.util.List;

import play.twirl.api.Html;

import com.google.common.collect.Lists;

import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

/**
 * 密码重置
 * 
 * @author xiaoch
 *
 */
public class MemberResetPasswordProvider implements
		IMemberAccountHomeFragmentProvider {

	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.BODY);
	}

	@Override
	public int getDisplayOrder() {
		return 71;
	}

	@Override
	public Html getFragment(String email, Position position) {
		if (this.getPosition().contains(position)) {
			return views.html.member.quickMenus.member_resetpassword_menu
					.render();
		} else {
			return null;
		}

	}
}
