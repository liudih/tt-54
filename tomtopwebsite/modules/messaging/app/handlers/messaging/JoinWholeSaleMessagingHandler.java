package handlers.messaging;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.member.MemberEmailService;
import services.messaging.MemberMessagingService;

import com.google.common.eventbus.Subscribe;

import context.ContextUtils;
import event.messaging.JoinWholeSaleMessagingEvent;

public class JoinWholeSaleMessagingHandler {
	@Inject
	MemberMessagingService messagingService;

	@Subscribe
	public void joinWholeSaleHandler(JoinWholeSaleMessagingEvent event) {
		try {
			messagingService.sendWholeSaleResultMessaging(event.getToemail(),
					event.getIstatus(), event.getIwebsiteid(), event.getIlanguagid(), event.getIsendiid(),
					ContextUtils.getWebContext(Context.current()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
