package dao.manager;

import java.util.List;

import valueobjects.manager.search.HistoryMsgContext;
import entity.manager.LivechatMsgInfo;

public interface ILivechatMsgInfoUpdateDao {
	int insert(LivechatMsgInfo info);

	List<LivechatMsgInfo> searchHistoryMsg(HistoryMsgContext context);

	int searchHistoryMsgCount(HistoryMsgContext context);

}
