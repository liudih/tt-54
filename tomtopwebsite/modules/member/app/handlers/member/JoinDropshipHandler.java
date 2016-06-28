package handlers.member;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.member.IMemberEmailService;

import com.google.common.eventbus.Subscribe;

import context.ContextUtils;
import events.member.JoinDropShipEvent;

public class JoinDropshipHandler {
	@Inject
	IMemberEmailService emailService;

	@Subscribe
	public void joinDropShipHandler(JoinDropShipEvent event) {
		try {
			emailService.sendDropShipResultEmail(event.getToemail(),
					event.getIstatus(), event.getIwebsiteid(),
					event.getIlanguageid(),
					ContextUtils.getWebContext(Context.current()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
