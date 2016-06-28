package extensions.loyalty.campaign.coupon;

import java.util.List;

import services.campaign.CampaignInstanceSupport;

import com.google.common.collect.Lists;

/**
 * 优惠券使用活动实例
 * @author lijun
 *
 */
public class CouponUseCampaignInstance extends CampaignInstanceSupport{
	private static final String ID = CouponUseAction.ID;
	
	public CouponUseCampaignInstance() {
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
		return Lists.newArrayList(CouponUseAction.ID);
	}
	
}
