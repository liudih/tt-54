package services.base;

import javax.inject.Inject;

import com.google.common.eventbus.EventBus;

import services.IEventBusService;


public class EventBusService implements IEventBusService{

	@Inject
	EventBus eventBus;
	
	@Override
	public void post(Object obj) {
		eventBus.post(obj);
	}
}
