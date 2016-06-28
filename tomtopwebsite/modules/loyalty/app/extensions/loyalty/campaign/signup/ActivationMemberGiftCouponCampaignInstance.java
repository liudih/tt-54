package extensions.loyalty.campaign.signup;

import java.util.List;

import services.campaign.CampaignInstanceSupport;

import com.google.common.collect.Lists;

/**
 * 用户通过邮箱激活账号时优惠券活动
 * 
 * @author lijun
 *
 */
public class ActivationMemberGiftCouponCampaignInstance extends CampaignInstanceSupport {
	public static final String ID = "activation-member-gift-coupon";
	
	public ActivationMemberGiftCouponCampaignInstance() {
		super.setInstanceId(ID);
	}

	@Override
	public void persist() {

	}

	/**
	 * 要执行action ids
	 * 
	 * @return
	 */
	public List<String> getActionIDs() {
		return Lists.newArrayList(ActivationMemberGiftCouponAction.ID);
	}
}
