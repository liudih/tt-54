package handlers.member;

import javax.inject.Inject;

import services.member.IMemberEmailService;

import com.google.common.eventbus.Subscribe;

import events.member.IntegralEvent;

public class IntegralHandler {

	@Inject
	IMemberEmailService emailService;

	@Subscribe
	public void prizeHandler(IntegralEvent event) {
		try {
			emailService.sendIntegralEmail(event.getEmail(),
					event.getIntegral(), event.getWebContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
