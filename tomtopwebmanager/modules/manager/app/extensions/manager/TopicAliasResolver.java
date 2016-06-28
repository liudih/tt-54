package extensions.manager;

import java.util.Collection;

import javax.inject.Inject;

import play.Logger;
import services.base.FoundationService;
import services.base.SystemParameterService;
import valueobjects.livechat.AliasResolution;
import valueobjects.livechat.Status;
import valueobjects.livechat.session.ChatSession;
import extensions.livechat.CustomerServiceFilter;
import extensions.livechat.LiveChatAliasResolver;

public class TopicAliasResolver implements LiveChatAliasResolver {

	@Inject
	CustomerServiceFilter customerServiceFilter;

	@Inject
	services.livechat.LiveChatService liveChatService;

	@Inject
	SystemParameterService parameterService;

	@Inject
	FoundationService foundation;

	@Override
	public boolean canResolve(String alias) {
		return alias.startsWith("topic:");
	}

	@Override
	public AliasResolution resolve(String alias, int siteID, int languageID) {
		String topic = alias.substring(6);
		// resolve topic to corresponding LTC
		Logger.debug("Resolving Topic {}, siteID {}, language {}", topic,
				siteID, languageID);
		int topicId = -1;
		try {
			topicId = Integer.valueOf(topic);
		} catch (Exception ex) {
			Logger.debug("invalid topic " + ex.getMessage());
			// unrecoverable error, dequeue
			return new AliasResolution(false);
		}
		String ltc = customerServiceFilter.getOptimalUser(topicId, languageID);
		if (null == ltc) {
			return new AliasResolution(ltc);
		}
		Collection<ChatSession> sessionList = this.liveChatService
				.getChatSessions(ltc);

		String allowMaxChatNumber = parameterService.getSystemParameter(
				foundation.getSiteID(), foundation.getLanguage(),
				"livechat.allowMaxChatNumber");
		if (null == allowMaxChatNumber || "".equals(allowMaxChatNumber)) {
			allowMaxChatNumber = "5";
		}

		int upperLimit = Integer.parseInt(allowMaxChatNumber);

		return new AliasResolution(ltc, upperLimit - sessionList.size());
	}

}
