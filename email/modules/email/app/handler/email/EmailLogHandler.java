package handler.email;

import javax.inject.Inject;

import service.email.EmailLogService;

import com.google.common.eventbus.Subscribe;

import events.email.EmailLogEvent;

public class EmailLogHandler {

	@Inject
	EmailLogService emailLogService;

	@Subscribe
	public void addEmailLog(EmailLogEvent emailLogEvent) {
		emailLogService.add(emailLogEvent);
	}
}
