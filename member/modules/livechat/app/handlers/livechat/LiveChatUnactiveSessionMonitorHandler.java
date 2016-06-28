package handlers.livechat;

import java.util.Collection;
import java.util.Date;

import javax.inject.Inject;

import play.Logger;
import services.livechat.LiveChatService;
import valueobjects.livechat.Status;
import valueobjects.livechat.control.HangupControl;
import valueobjects.livechat.session.ChatSession;

import com.google.common.collect.Collections2;
import com.google.common.eventbus.Subscribe;

import events.livechat.LiveChatUnactiveSessionEvent;

public class LiveChatUnactiveSessionMonitorHandler {

	@Inject
	LiveChatStatusMonitor liveChatStatusMonitor;

	@Inject
	LiveChatService liveChatService;

	@Subscribe
	public void removeSession(LiveChatUnactiveSessionEvent event) {
		Collection<ChatSession> currentslist = liveChatService.getAllSession();
		if (null == currentslist || currentslist.size() == 0)
			return;
		Logger.debug("--------> session count --{} ", currentslist.size());
		// ~ find unactive sessions
		Collection<ChatSession> unactiveSessions = Collections2.filter(
				currentslist,
				session -> {
					Status originStatus = liveChatStatusMonitor
							.checkStatusByLtc(session.getOrigin());
					Status destinationStatus = liveChatStatusMonitor
							.checkStatusByLtc(session.getDestination());
					return originStatus == null || destinationStatus == null;
				});
		// ~ remove unactive session
		for (ChatSession cs : unactiveSessions) {
			Logger.debug("-------->remove sessions----" + cs.getId());
			liveChatService.hangup(cs.getOrigin(),
					new HangupControl(cs.getId()));
		}

	}
}
