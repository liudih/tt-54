package extensions.livechat.role;

import java.util.Collection;

import javax.inject.Inject;

import play.Logger;

import com.google.common.collect.Collections2;

import services.livechat.LiveChatService;
import valueobjects.livechat.Status;
import valueobjects.livechat.session.ChatSession;
import valueobjects.livechat.status.ConversationStatus;
import valueobjects.livechat.status.LiveChatStatus;
import valueobjects.livechat.status.NoneStatus;

public class LtcSessionStatusProvider implements LiveChatRoleStatusProvider {

	@Inject
	LiveChatService liveChatService;

	@Override
	public boolean canResolve(String alias) {
		return true;
	}

	@Override
	public LiveChatStatus getStatus(String alias, String ltc, int languageid) {

		Logger.debug("get ltc sessions {}", ltc);

		Collection<ChatSession> list = Collections2.filter(
				liveChatService.getAllSession(), obj -> obj.getDestination()
						.equals(ltc) || obj.getOrigin().equals(ltc));
		if (null != list && list.size() > 0) {
			Logger.debug(" ltc {} had in the session!", ltc);
			return new ConversationStatus();
		}

		return new NoneStatus();
	}

}
