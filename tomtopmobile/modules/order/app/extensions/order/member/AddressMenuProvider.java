package extensions.order.member;

import java.util.List;

import javax.inject.Inject;



import com.google.common.collect.Lists;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.order.IOrderCountService;
import valueobjects.member.MemberInSession;
import valueobjects.order_api.OrderCount;
import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class AddressMenuProvider implements
		IMemberAccountHomeFragmentProvider {

	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.BODY);
	}

	@Override
	public int getDisplayOrder() {
		return 100;
	}

	@Override
	public Html getFragment(String email, Position position) {
		if(this.getPosition().contains(position)){
			return views.html.mobile.order.member.address_menu.render();
		}else{
			return null;
		}
	}

}
