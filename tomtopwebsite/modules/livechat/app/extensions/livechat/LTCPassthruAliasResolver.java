package extensions.livechat;

import java.util.Collection;

import javax.inject.Inject;

import services.base.FoundationService;
import services.base.SystemParameterService;
import valueobjects.livechat.AliasResolution;
import valueobjects.livechat.session.ChatSession;

/**
 * Resolve the destination of the format "ltc:xxxxxxx", good to be used for
 * testing purpose.
 * 
 * @author kmtong
 *
 */
public class LTCPassthruAliasResolver implements LiveChatAliasResolver {

	@Inject
	FoundationService foundation;
	@Inject
	SystemParameterService parameterService;
	@Inject
	services.livechat.LiveChatService liveChatService;

	@Override
	public boolean canResolve(String alias) {
		return alias.startsWith("ltc:");
	}

	@Override
	public AliasResolution resolve(String alias, int siteID, int languageID) {
		String allowMaxChatNumber = parameterService.getSystemParameter(
				foundation.getSiteID(), foundation.getLanguage(),
				"livechat.allowMaxChatNumber");
		if (null == allowMaxChatNumber || "".equals(allowMaxChatNumber)) {
			allowMaxChatNumber = "5";
		}
		Collection<ChatSession> sessionList = this.liveChatService
				.getChatSessions(alias.substring(4));

		return new AliasResolution(alias.substring(4),
				Integer.parseInt(allowMaxChatNumber) - sessionList.size());
	}

}
