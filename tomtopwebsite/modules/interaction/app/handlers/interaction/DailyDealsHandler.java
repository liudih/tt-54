package handlers.interaction;

import services.interaction.dailydeal.DailyDealsService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.interaction.DailyDealsEvent;

public class DailyDealsHandler {

	@Inject
	DailyDealsService dailyDealsService;

	@Subscribe
	public void getDailyDeals(DailyDealsEvent event) {
		if (null == event.getType()) {
			dailyDealsService.getDailyDeals(event.getWebsiteId(),
					event.getCurrency(), event.isInitDate());
		} else {
			dailyDealsService.updateDailyDeals(event.getWebsiteId());
		}
	}

}
