package handlers.member;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.member.IMemberEmailService;

import com.google.common.eventbus.Subscribe;

import context.ContextUtils;
import events.member.JoinDropShipEvent;
import events.member.ReplyMemberFaqEvent;

public class ReplyMemberFaqHandler {
	@Inject
	IMemberEmailService emailService;

	@Subscribe
	public void replyMemberFaqEmail(ReplyMemberFaqEvent event) {
		try {
			emailService.sendReplyMemberFaqEmail(event.getToemail(),
					event.getQuestion(), event.getAnswer(),
					event.getIwebsiteid(), event.getIlanguageid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
