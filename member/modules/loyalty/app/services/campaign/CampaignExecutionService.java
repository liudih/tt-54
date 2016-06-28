package services.campaign;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import play.Logger;
import play.libs.F.Tuple;
import services.base.utils.Utils;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CampaignExecutionService {

	@Inject
	Set<ICampaign> campaigns;

	public List<ICampaign> lookup(Object payload) {
		return FluentIterable.from(campaigns)
				.filter(c -> c.getPayloadClass() == payload.getClass())
				.toList();
	}

	public List<ICampaignInstance> execute(Object payload) {
		List<ICampaign> campaigns = lookup(payload);
		if (campaigns.size() == 0)
			return Lists.newLinkedList();

		Logger.debug("Potential Campaigns for {}: {}", payload.getClass(),
				Lists.transform(campaigns, c -> c.getId()));
		List<ICampaignInstance> cinstances = Utils.flatten(Lists.transform(
				campaigns, (ICampaign c) -> c.getActiveInstances(payload)));

		Map<Tuple<String, String>, ICampaignInstance> instanceMap = Maps
				.uniqueIndex(cinstances, ci -> new Tuple<String, String>(ci
						.getCampaign().getId(), ci.getInstanceId()));

		Map<Tuple<String, String>, List<IActionRuleParameter>> ruleParams = Maps
				.transformValues(instanceMap, i -> i.getActionRuleParams());

		Map<Tuple<String, String>, CampaignContext> contextMap = Maps
				.transformValues(instanceMap, ci -> ci.getCampaign()
						.createCampaignContext(payload, ci));

		Map<Tuple<String, String>, ICampaignInstance> instancesSatisfied = Maps
				.filterValues(
						instanceMap,
						i -> i.getActionRules() == null
								|| i.getActionRules()
										.match(contextMap
												.get(new Tuple<String, String>(
														i.getCampaign().getId(),
														i.getInstanceId())),
												ruleParams
														.get(new Tuple<String, String>(
																i.getCampaign()
																		.getId(),
																i.getInstanceId()))));

		List<ICampaignInstance> executedInstance = Lists.newLinkedList();
		for (Entry<Tuple<String, String>, ICampaignInstance> ce : instancesSatisfied
				.entrySet()) {
			Tuple<String, String> id = ce.getKey();
			Logger.debug("Executing Actions for {}-{}: ", id._1, id._2);
			ICampaignInstance c = ce.getValue();
			executedInstance.add(c);
			Map<String, IActionParameter> actionParams = Maps.uniqueIndex(
					c.getActionParams(), ap -> ap.getActionId());
			for (IAction a : c.getActions()) {
				a.execute(contextMap.get(id), actionParams.get(a.getId()));
			}
		}
		return executedInstance;
	}

	public Optional<ICampaign> getCampaign(String campaignId) {
		return FluentIterable.from(campaigns)
				.filter(c -> c.getId().equals(campaignId)).first();
	}

	public Optional<ICampaignInstance> getCampaignInstance(String campaignId,
			String instanceId) {
		try {
			return getCampaign(campaignId).transform(
					c -> c.getInstance(instanceId).orNull());
		} catch (NullPointerException e) {
			return Optional.absent();
		}
	}

	public Optional<IAction> getAction(String campaignId, String instanceId,
			String actionId) {
		try {
			List<IAction> actions = getCampaign(campaignId).transform(
					c -> c.getInstance(instanceId)
							.transform(i -> i.getActions()).orNull()).orNull();
			if (actions != null) {
				return FluentIterable.from(actions)
						.filter(a -> a.getId().equals(actionId)).first();
			}
			return Optional.absent();
		} catch (NullPointerException e) {
			return Optional.absent();
		}
	}

}
