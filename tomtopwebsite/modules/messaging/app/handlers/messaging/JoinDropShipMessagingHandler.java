package handlers.messaging;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.messaging.MemberMessagingService;

import com.google.common.eventbus.Subscribe;

import context.ContextUtils;
import event.messaging.JoinDropShipMessagingEvent;

public class JoinDropShipMessagingHandler {
	@Inject
	MemberMessagingService messagingService;

	@Subscribe
	public void joinWholeSaleHandler(JoinDropShipMessagingEvent event) {
		try {
			messagingService.sendDropShipResultMessaging(event.getToemail(),
					event.getIstatus(), event.getIwebsiteid(),
					event.getIlanguagid(), event.getIsendiid(),
					ContextUtils.getWebContext(Context.current()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
