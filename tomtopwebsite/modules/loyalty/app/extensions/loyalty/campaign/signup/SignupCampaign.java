package extensions.loyalty.campaign.signup;

import java.util.List;

import javax.inject.Inject;

import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.ICampaignInstance;
import valueobjects.member.MemberIdentification;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import events.member.RegistrationEvent;
import extensions.loyalty.campaign.action.point.GrantPointAction;
import extensions.loyalty.campaign.action.point.GrantPointActionParameter;

public class SignupCampaign extends CampaignSupport {

	@Inject
	CampaignContextFactory ctxFactory;

	@Override
	public String getId() {
		return "signup";
	}

	@Override
	public Class<?> getPayloadClass() {
		return RegistrationEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		RegistrationEvent event = (RegistrationEvent) payload;
		CampaignContext ctx = ctxFactory.createContext(payload, instance);
		MemberIdentification id = new MemberIdentification(event.getEmail(),
				event.getSiteID());
		ctx.setActionOn(id);
		return ctx;
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return new SignupCampaignInstance();
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

	private SignupCampaignInstance fixInstance() {
		SignupCampaignInstance i = new SignupCampaignInstance();
		i.setInstanceId("signup-points");
		i.setCampaign(this);
		i.setActions(getPossibleActions());
		// hard-code 50 points
		GrantPointActionParameter p = new GrantPointActionParameter();
		p.setSource("register");
		p.setPoints(200);
		i.setActionParams(Lists.newArrayList(p));
		return i;
	}
}
