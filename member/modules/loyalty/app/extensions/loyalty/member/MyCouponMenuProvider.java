package extensions.loyalty.member;

import play.twirl.api.Html;
import services.base.FoundationService;
import services.loyalty.coupon.ICouponMainService;
import valueobjects.base.LoginContext;

import com.google.inject.Inject;

import extensions.member.account.IMemberAccountMenuProvider;

/**
 * Account->My Coupons
 * 
 * @author lijun
 *
 */
public class MyCouponMenuProvider implements IMemberAccountMenuProvider {
	private static final String ID = "my-coupon";

	@Inject
	private ICouponMainService service;

	@Inject
	private FoundationService fservice;

	@Override
	public String getCategory() {
		return "account";
	}

	@Override
	public int getDisplayOrder() {
		return 22;
	}

	@Override
	public Html getMenuItem(String currentMenuID) {
		LoginContext context = fservice.getLoginContext();
		String userEmail = context.getMemberID();
		Integer total = service.getTotalMyCouponUnused(userEmail);
		Object url = controllers.loyalty.routes.MyCoupon.list(1,15);
		// controllers.loyalty.routes.MyCoupon.list(1,15);
		if (this.ID.equals(currentMenuID)) {
			return Html.apply("<li class='leftAct'><a href='" + url.toString()
					+ "'> My Coupons(" + total + ")</a></li>");
		} else {
			return Html.apply("<li><a href='" + url.toString()
					+ "'> My Coupons(" + total + ")</a></li>");
		}
	}

}
