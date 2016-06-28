package handlers.member;

import javax.inject.Inject;

import services.member.IMemberEmailService;

import com.google.common.eventbus.Subscribe;

import events.member.PrizeEvent;

public class PrizeHandler {

	@Inject
	IMemberEmailService emailService;

	@Subscribe
	public void prizeHandler(PrizeEvent event) {
		try {
			emailService.sendPrizeEmail(event.getToemail(), event.getTitle(),
					event.getContext(), event.getWebContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
