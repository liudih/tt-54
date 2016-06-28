package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import valueobjects.manager.search.HistoryMsgContext;
import mapper.manager.LivechatMsgInfoMapper;
import dao.manager.ILivechatMsgInfoUpdateDao;
import entity.manager.LivechatMsgInfo;

public class LivechatMsgInfoUpdateDao implements ILivechatMsgInfoUpdateDao {
	@Inject
	LivechatMsgInfoMapper mapper;

	@Override
	public int insert(LivechatMsgInfo info) {
		return mapper.insert(info);
	}

	@Override
	public List<LivechatMsgInfo> searchHistoryMsg(HistoryMsgContext context) {
		return mapper.searchHistoryMsgPage(context);
	}

	@Override
	public int searchHistoryMsgCount(HistoryMsgContext context) {
		return mapper.searchHistoryMsgCount(context);
	}

}
