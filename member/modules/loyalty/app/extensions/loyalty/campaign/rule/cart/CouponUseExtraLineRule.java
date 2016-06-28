package extensions.loyalty.campaign.rule.cart;

import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import valueobjects.order_api.cart.IExtraLineRule;
import extensions.loyalty.campaign.coupon.CouponUseAction;
import extensions.loyalty.orderxtras.CouponExtrasProvider;

/**
 * 优惠券使用后推广码就不能再用 所以要排除推广码插件的使用
 * 
 * @author lijun
 *
 */
public class CouponUseExtraLineRule implements IExtraLineRule {
	@Inject
	CouponExtrasProvider couponExtrasProvider;

	@Override
	public RuleType getRuleType() {
		return IExtraLineRule.RuleType.EXCLUDE_PART;
	}

	@Override
	public List<String> getExcludePluginIDs() {
		return Lists.newArrayList(couponExtrasProvider.getId());
	}

	@Override
	public String getPluginId() {
		return CouponUseAction.ID;
	}

}
