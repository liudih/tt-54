package handlers.member;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.member.IMemberEmailService;

import com.google.common.eventbus.Subscribe;

import context.ContextUtils;
import events.member.JoinWholeSaleEvent;

public class JoinWholeSaleHandler {
	@Inject
	IMemberEmailService emailService;

	@Subscribe
	public void joinWholeSaleHandler(JoinWholeSaleEvent event) {
		try {
			emailService.sendWholeSaleResultEmail(event.getToemail(),
					event.getIstatus(), event.getIwebsiteid(),
					event.getIlanguageid(),
					ContextUtils.getWebContext(Context.current()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
