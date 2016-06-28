package extensions.loyalty.campaign.orderpayment;

import java.util.List;

import javax.inject.Inject;

import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.ICampaignInstance;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import events.order.PaymentConfirmationEvent;
import extensions.loyalty.campaign.action.point.GrantPointAction;
import extensions.loyalty.campaign.action.point.GrantPointActionParameter;

public class OrderPaymentCampaign extends CampaignSupport {

	@Inject
	CampaignContextFactory ctxFactory;

	@Override
	public String getId() {
		return "order-payment";
	}

	@Override
	public Class<?> getPayloadClass() {
		return PaymentConfirmationEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		PaymentConfirmationEvent event = (PaymentConfirmationEvent) payload;
		CampaignContext ctx = ctxFactory.createContext(payload, instance);
		ctx.setActionOn(event.getOrderValue());
		return ctx;
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return new OrderPaymentCampaignInstance();
	}

	@Override
	public List<ICampaignInstance> getActiveInstances(Object payload) {
		return Lists.newArrayList(fixInstance());
	}

	@Override
	public Optional<ICampaignInstance> getInstance(String instanceId) {
		return Optional.of(fixInstance());
	}

	protected List<String> getPossibleActionRuleIDs() {
		return Lists.newArrayList();
	}

	protected List<String> getPossibleActionIDs() {
		return Lists.newArrayList(GrantPointAction.ID);
	}

	private OrderPaymentCampaignInstance fixInstance() {
		OrderPaymentCampaignInstance i = new OrderPaymentCampaignInstance();
		i.setInstanceId("order-payment-points");
		i.setCampaign(this);
		i.setActions(getPossibleActions());
		// XXX hard-code US1 = 1 point
		GrantPointActionParameter p = new GrantPointActionParameter();
		p.setRate(1);
		p.setSource("order-payment");
		i.setActionParams(Lists.newArrayList(p));
		return i;
	}
}
