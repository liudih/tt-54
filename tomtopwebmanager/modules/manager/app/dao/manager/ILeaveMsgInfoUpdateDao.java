package dao.manager;

import entity.manager.LeaveMsgInfo;

public interface ILeaveMsgInfoUpdateDao {
	int insert(LeaveMsgInfo info);

	int leaveMsgInfoHandle(String handler, Integer iid);
	
	int updateById(int replyuserid, String replycontent, int id);
}
