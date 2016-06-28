package extensions.loyalty.campaign.signup;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.base.Optional;

import events.member.ActivationEvent;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.IAction;
import services.campaign.ICampaignInstance;

/**
 * 用户通过邮箱激活账号时优惠券活动
 * 
 * @author lijun
 *
 */
public class ActivationMemberCampaign extends CampaignSupport {
	public static final String ID = "activation-member";

	@Inject
	CampaignContextFactory ctxFactory;

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public Class<?> getPayloadClass() {
		return ActivationEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		return ctxFactory.createContext(payload, instance);
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return null;
	}

	@Override
	public List<ICampaignInstance> getActiveInstances(Object payload) {
		ActivationMemberGiftCouponCampaignInstance ci = new ActivationMemberGiftCouponCampaignInstance();
		List<String> actionIds = ci.getActionIDs();
		List<IAction> actions = super.getActions(actionIds);
		ci.setActions(actions);
		ci.setCampaign(this);
		return Lists.newArrayList(ci);
	}

	@Override
	public Optional<ICampaignInstance> getInstance(String instanceId) {
		return null;
	}

	@Override
	protected List<String> getPossibleActionRuleIDs() {
		return null;
	}

	@Override
	protected List<String> getPossibleActionIDs() {
		return Lists.newArrayList(ActivationMemberGiftCouponAction.ID);
	}

}
