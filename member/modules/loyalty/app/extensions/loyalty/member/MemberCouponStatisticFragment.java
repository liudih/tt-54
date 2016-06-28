package extensions.loyalty.member;

import play.Logger;
import play.twirl.api.Html;
import services.loyalty.coupon.ICouponMainService;
import valueobjects.member.MemberInSession;

import com.google.inject.Inject;

import extensions.member.account.IMemberAccountHomeFragmentProvider;
import extensions.member.account.Position;

public class MemberCouponStatisticFragment implements
		IMemberAccountHomeFragmentProvider {
	//add by lijun
	@Inject
	private ICouponMainService couponService;
	
	@Override
	public Position getPosition() {
		return Position.STATISTICS;
	}

	@Override
	public int getDisplayOrder() {
		return 20;
	}

	@Override
	public Html getFragment(MemberInSession member) {
		String userEmail = member.getEmail();
		int total = 0;
		try {
			total = couponService.getTotalMyCouponUnused(userEmail);
		} catch (Exception e) {
			if(Logger.isInfoEnabled()){
				Logger.info("getTotalMyCouponUnused failed",e);
			}
		}
		
		return views.html.loyalty.member.coupon.render(total);
	}

}
