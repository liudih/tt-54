package extensions.loyalty.campaign.action.order.givingcoupon;

import java.util.List;

import play.Logger;
import play.cache.Cache;
import codec.ICodec;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import dto.order.OrderDetail;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.campaign.CampaignContext;
import services.campaign.IAction;
import services.campaign.IActionParameter;
import services.loyalty.coupon.CouponSkuService;
import services.loyalty.coupon.impl.CouponService;
import valueobjects.order_api.OrderValue;

public class GivingCouponAction implements IAction {

	@Inject
	CouponSkuService couponSkuService;

	@Inject
	CouponService couponService;

	@Inject
	FoundationService foundationService;

	public final static String ID = "giving-coupon";

	public final static String GIVINGCOUPON_CACHE = "giving_coupon_";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public void execute(CampaignContext context, IActionParameter param) {
		OrderValue order = (OrderValue) context.getActionOn();
		List<OrderDetail> orderDetails = order.getDetails();
		List<Integer> ruleIds = couponSkuService
				.getRuleIdsByOrderdetail(orderDetails);
		String email = order.getOrder().getCmemberemail();
		if (StringUtils.isEmpty(email)) {
			email = order.getOrder().getCemail();
			Logger.info(
					"giving coupon--->member is null,get other email=={},orderNum=={}",
					email, order.getOrder().getCordernumber());
		}

		if (null != ruleIds && ruleIds.size() > 0) {
			String key = GIVINGCOUPON_CACHE
					+ order.getOrder().getCordernumber();
			Object isGived = Cache.get(key);
			if (null != isGived) {
				Logger.info("giving coupon--->has been gived,orderNum=={}",
						order.getOrder().getCordernumber());
				return;
			} else {
				Cache.set(key, 1, 1800);
			}
			for (int i = 0; i < ruleIds.size(); i++) {
				Logger.info(
						"*********---giving-coupon---**********ruleid=={},email=={}",
						String.valueOf(ruleIds.get(i)), email);
				couponService.createCouponByRuleNoContext(email,
						ruleIds.get(i), order.getOrder().getIwebsiteid());
			}
		} else {
			Logger.error(
					"giving coupon-->can not get relation rule,orderNum=={}",
					order.getOrder().getCordernumber());
		}
	}

	@Override
	public ICodec<IActionParameter, JsonNode> getParameterCodec() {
		return null;
	}

}
