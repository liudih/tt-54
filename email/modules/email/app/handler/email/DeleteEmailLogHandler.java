package handler.email;

import javax.inject.Inject;

import service.email.EmailLogService;

import com.google.common.eventbus.Subscribe;

import events.email.DeleteEmailLogEvent;

public class DeleteEmailLogHandler {

	@Inject
	EmailLogService emailLogService;

	@Subscribe
	public void deleteEmailLog(DeleteEmailLogEvent deleteEmailLogEvent) {
		emailLogService.deleteEmailLogRegular();
	}
}
