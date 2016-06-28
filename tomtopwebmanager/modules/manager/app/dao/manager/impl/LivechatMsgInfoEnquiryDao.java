package dao.manager.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dao.manager.ILivechatMsgInfoEnquiryDao;
import dto.LivechatSessionMsgStatistics;
import mapper.manager.LivechatMsgInfoMapper;
import valueobjects.manager.LivechatSessionStatistics;

public class LivechatMsgInfoEnquiryDao implements ILivechatMsgInfoEnquiryDao {

	@Inject
	LivechatMsgInfoMapper mapper;

	@Override
	public List<LivechatSessionStatistics> getUserSessionCount(Date fromDate,
			List<String> users) {
		return mapper.getUserSessionCount(fromDate, users);
	}

	@Override
	public List<LivechatSessionMsgStatistics> getSessionStatistics(Date beginDate,
			Date endDate, String dateFormat,String username) {
		return mapper.getSessionStatistics(beginDate, endDate, dateFormat,username);
	}
}
