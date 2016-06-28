package extensions.loyalty.campaign;

import java.util.List;

import javax.inject.Inject;

import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.IAction;
import services.campaign.ICampaignInstance;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import events.subscribe.SubscribeEvent;
import extensions.loyalty.campaign.subscription.EDMSubscriptionGiftCouponCampaignInstance;

public class EDMSubscriptionCampaign extends CampaignSupport {

	@Inject
	CampaignContextFactory ctxFactory;
	
	@Override
	public Class<SubscribeEvent> getPayloadClass() {
		return SubscribeEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		return ctxFactory.createContext(payload, instance);
	}

	@Override
	public String getId() {
		return "edm-subscription-reward";
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return null;
	}

	@Override
	public List<ICampaignInstance> getActiveInstances(Object payload) {
		EDMSubscriptionGiftCouponCampaignInstance ci = new EDMSubscriptionGiftCouponCampaignInstance();
		ci.setCampaign(this);
		List<String> actionIds = ci.getActionIDs();
		List<IAction> actions = super.getActions(actionIds);
		ci.setActions(actions);
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
		return null;
	}
}
