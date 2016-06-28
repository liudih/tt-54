package handlers.loyalty;

import java.util.List;

import javax.inject.Inject;

import play.Logger;
import services.campaign.CampaignExecutionService;
import services.campaign.ICampaignInstance;

import com.google.common.eventbus.Subscribe;

public class CampaignEventHandler {

	@Inject
	CampaignExecutionService campaignExec;

	@Subscribe
	public void onAnyEvent(Object event) {
		Logger.trace("Campaign Execution Check: {}", event.getClass().getName());
		try {
			List<ICampaignInstance> instances = campaignExec.execute(event);
			if (instances != null && !instances.isEmpty()) {
				Logger.debug("Campaign Executed: {}", instances);
			}
		} catch (Throwable e) {
			Logger.error("Campaign Execution Error", e);
		}
	}
}
