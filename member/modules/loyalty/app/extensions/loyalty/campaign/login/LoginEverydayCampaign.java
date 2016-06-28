package extensions.loyalty.campaign.login;

import java.util.List;

import javax.inject.Inject;

import services.campaign.ActionRules;
import services.campaign.CampaignContext;
import services.campaign.CampaignContextFactory;
import services.campaign.CampaignSupport;
import services.campaign.ICampaignInstance;
import services.campaign.MultiRules.Match;
import valueobjects.member.MemberIdentification;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import events.member.LoginEvent;
import extensions.loyalty.campaign.action.point.GrantPointAction;
import extensions.loyalty.campaign.action.point.GrantPointActionParameter;
import extensions.loyalty.campaign.rule.firstloginperday.FirstLoginPerDayActionRule;

public class LoginEverydayCampaign extends CampaignSupport {

	@Inject
	CampaignContextFactory ctxFactory;

	@Override
	public String getId() {
		return "login-everyday";
	}

	@Override
	public Class<?> getPayloadClass() {
		return LoginEvent.class;
	}

	@Override
	public CampaignContext createCampaignContext(Object payload,
			ICampaignInstance instance) {
		LoginEvent event = (LoginEvent) payload;
		CampaignContext ctx = ctxFactory.createContext(payload, instance);
		MemberIdentification id = new MemberIdentification(event.getEmail(),
				event.getSiteID());
		ctx.setActionOn(id);
		return ctx;
	}

	@Override
	public ICampaignInstance createCampaignInstance() {
		return new LoginEverydayCampaignInstance();
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
		return Lists.newArrayList(FirstLoginPerDayActionRule.ID);
	}

	protected List<String> getPossibleActionIDs() {
		return Lists.newArrayList(GrantPointAction.ID);
	}

	private LoginEverydayCampaignInstance fixInstance() {
		LoginEverydayCampaignInstance i = new LoginEverydayCampaignInstance();
		i.setInstanceId("login-everyday-points");
		i.setCampaign(this);
		i.setActions(getPossibleActions());
		// hard-code 5 points
		GrantPointActionParameter p = new GrantPointActionParameter();
		p.setPoints(5);
		i.setActionParams(Lists.newArrayList(p));

		i.setActionRules(new ActionRules(getPossibleActionRules(),
				Match.MATCH_ALL));
		return i;
	}
}
