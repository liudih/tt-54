package extensions.livechat.role;

import valueobjects.livechat.status.LiveChatStatus;

public interface LiveChatRoleStatusProvider {

	public boolean canResolve(String alias);

	/**
	 * 状态
	 * 
	 * @return
	 */
	LiveChatStatus getStatus(String alias, String ltc, int languageid);
}
