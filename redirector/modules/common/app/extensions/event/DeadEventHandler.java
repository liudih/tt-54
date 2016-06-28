package extensions.event;

import play.Logger;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

public class DeadEventHandler {

	@Subscribe
	public void deadEvent(DeadEvent e) {
		Logger.debug("Event not handled: {}", e.getEvent());
	}
}
