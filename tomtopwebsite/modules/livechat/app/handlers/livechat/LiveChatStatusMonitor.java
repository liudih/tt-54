package handlers.livechat;

import java.util.Date;

import javax.inject.Inject;

import play.Logger;
import services.livechat.LiveChatService;
import valueobjects.livechat.Status;

public class LiveChatStatusMonitor {

	/**
	 * 过期时长 毫秒
	 */
	final Long expireSecond = 30000l;

	@Inject
	LiveChatService liveChatService;

	/**
	 * 检查 status是否还有效，无效叫删除
	 * 
	 * @param alias
	 */
	public Status checkStatusByAlias(String alias) {
		Status st = liveChatService.getStatusByAlias(alias);
		if (st != null && needDelete(st.getLastSeen())) {
			Logger.debug("remove status ->alias {}  ltc {}", st.getAlias(),
					st.getLtc());
			liveChatService.detetStatus(st.getLtc());
			return null;
		}
		return st;
	}

	/**
	 * 检查 status是否还有效，无效叫删除
	 * 
	 * @param ltc
	 */
	public Status checkStatusByLtc(String ltc) {
		Status st = liveChatService.getStatusByLTC(ltc);
		if (st != null && needDelete(st.getLastSeen())) {
			Logger.debug("remove status ->alias {}  ltc {}", st.getAlias(),
					st.getLtc());
			liveChatService.detetStatus(st.getLtc());
			return null;
		}
		return st;
	}

	private boolean needDelete(Date lastseen) {
		long nowTime = new Date().getTime();
		return (nowTime - lastseen.getTime()) > expireSecond;
	}
}
