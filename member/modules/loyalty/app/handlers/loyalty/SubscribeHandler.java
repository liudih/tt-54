package handlers.loyalty;

import play.Logger;

import com.google.common.eventbus.Subscribe;

import events.subscribe.SubscribeEvent;

public class SubscribeHandler {

	
	@Subscribe
	public void onSubscribe(SubscribeEvent event) throws Exception {
		Logger.debug("_++"+event.getEmail());
		Logger.debug("_+2+"+event.getCategoryNames().toString());
	}
	
}
