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

public class OrderStatisticsProvider implements
		IMemberAccountHomeFragmentProvider {

	@Inject
	FoundationService foundationService;
	
	@Inject
	IOrderCountService countService;
	
	@Override
	public List<Position> getPosition() {
		return Lists.newArrayList(Position.STATISTICS);
	}

	@Override
	public int getDisplayOrder() {
		return 10;
	}

	@Override
	public Html getFragment(String email, Position position) {
		if(this.getPosition().contains(position)){
			OrderCount count = countService.getCountByEmail(email,
					foundationService.getSiteID(), 1, true);
			return views.html.mobile.order.member.statistics.order_stat.render(count);
		}else{
			return null;
		}
		
	}

}
