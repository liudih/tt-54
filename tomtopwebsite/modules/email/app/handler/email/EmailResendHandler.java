package handler.email;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;

import email.util.EmailSpreadUtil;
import events.email.EmailResendEvent;

public class EmailResendHandler {

	@Inject
	EmailSpreadUtil emailSpreadUtil;

	public static final int RESEND_TIME = 3;

	@Subscribe
	public void resendEmail(EmailResendEvent emailResendEvent) {
		String from = emailResendEvent.getFrom();
		String toEmail = emailResendEvent.getToEmail();
		String title = emailResendEvent.getTitle();
		String content = emailResendEvent.getContent();
		boolean result = false;
		for (int i = 0; i < RESEND_TIME; i++) {
			result = emailSpreadUtil.send(from, toEmail, "", title, content);;
			if (result == true) {
				break;
			}
		}

	}
}
