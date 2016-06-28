package handlers.base;

import play.Logger;
import services.base.ShorturlService;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import events.base.VisitEvent;

public class VisitHandler {
	
	@Inject
	ShorturlService ShorturlService;
	
	@Subscribe
	public void onVisit(VisitEvent event) {
		boolean flag = ShorturlService.addVisit(event);
		Logger.debug("add visit==={}",flag);
	}
}
