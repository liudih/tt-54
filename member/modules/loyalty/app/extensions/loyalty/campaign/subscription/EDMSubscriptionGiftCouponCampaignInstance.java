package extensions.loyalty.campaign.subscription;

import java.util.List;

import services.campaign.CampaignInstanceSupport;

import com.google.common.collect.Lists;

/**
 * 用户邮件订阅送优惠券活动
 * 
 * @author lijun
 *
 */
public class EDMSubscriptionGiftCouponCampaignInstance extends
		CampaignInstanceSupport {
	public static final String ID = "edm-subscription-reward-gift-coupon";

	public EDMSubscriptionGiftCouponCampaignInstance() {
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
		return Lists.newArrayList(EDMSubscriptionGiftConponAction.ID);
	}
}
