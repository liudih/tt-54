package extensions.manager;

import javax.inject.Inject;

import play.Logger;
import valueobjects.livechat.status.LiveChatStatus;
import valueobjects.livechat.status.NoServiceStatus;
import valueobjects.livechat.status.NoneStatus;
import extensions.livechat.CustomerServiceFilter;
import extensions.livechat.role.LiveChatRoleStatusProvider;

public class CustomerServiceStatusProvider implements
		LiveChatRoleStatusProvider {

	@Inject
	CustomerServiceFilter customerServiceFilter;

	@Override
	public boolean canResolve(String alias) {
		return alias.startsWith("topic:");
	}

	@Override
	public LiveChatStatus getStatus(String alias, String ltc, int languageid) {
		String topic = alias.substring(6);
		// resolve topic to corresponding LTC
		Logger.debug("status Topic {}, language {}", topic, languageid);
		int topicId = -1;
		NoneStatus nstatus = new NoneStatus();
		try {
			topicId = Integer.valueOf(topic);
		} catch (Exception ex) {
			Logger.debug("invalid topic " + ex.getMessage());
			return nstatus;
		}
		if (null == customerServiceFilter.getOnlineUsers(topicId, languageid)) {
			return new NoServiceStatus();
		}
		return nstatus;
	}

}
