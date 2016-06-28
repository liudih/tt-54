package extensions.loyalty.campaign.rule.cart;

import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Lists;

import valueobjects.order_api.cart.IExtraLineRule;
import extensions.loyalty.campaign.coupon.CouponUseAction;
import extensions.loyalty.orderxtras.CouponExtrasProvider;

/**
 * 推广码使用后优惠券就不能再用 所以要排除优惠券插件的使用
 * 
 * @author lijun
 *
 */
public class CouponCodeExtraLineRule implements IExtraLineRule {
	@Inject
	CouponExtrasProvider couponExtrasProvider;

	@Override
	public RuleType getRuleType() {
		return IExtraLineRule.RuleType.EXCLUDE_PART;
	}

	@Override
	public List<String> getExcludePluginIDs() {
		return Lists.newArrayList(CouponUseAction.ID);
	}

	@Override
	public String getPluginId() {
		return couponExtrasProvider.getId();
	}
}
