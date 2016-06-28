package dao.manager.impl;

import javax.inject.Inject;

import mapper.manager.LeaveMsgInfoMapper;
import dao.manager.ILeaveMsgInfoUpdateDao;
import entity.manager.LeaveMsgInfo;

public class LeaveMsgInfoUpdateDao implements ILeaveMsgInfoUpdateDao {
	@Inject
	LeaveMsgInfoMapper mapper;

	@Override
	public int insert(LeaveMsgInfo info) {
		return mapper.insert(info);
	}

	@Override
	public int leaveMsgInfoHandle(String handler, Integer iid) {
		return mapper.leaveMsgInfoHandle(handler, iid);
	}

	@Override
	public int updateById(int replyuserid, String replycontent, int id) {
		return mapper.updateById(replyuserid, replycontent, id);
	}

}
