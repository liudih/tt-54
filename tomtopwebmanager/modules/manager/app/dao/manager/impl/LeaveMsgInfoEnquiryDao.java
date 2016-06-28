package dao.manager.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import valueobjects.manager.LeaveMsgCount;
import valueobjects.manager.LivechatSessionStatistics;
import mapper.manager.LeaveMsgInfoMapper;
import dao.manager.ILeaveMsgInfoEnquiryDao;
import dto.LivechatLeaveMsgStatistics;
import entity.manager.LeaveMsgInfo;

public class LeaveMsgInfoEnquiryDao implements ILeaveMsgInfoEnquiryDao {
	@Inject
	LeaveMsgInfoMapper mapper;

	@Override
	public List<LeaveMsgInfo> getLeaveMsgInfoPage(int page, int pageSize) {
		return mapper.getPage(page, pageSize);
	}

	@Override
	public int getCount() {
		return mapper.getCount();
	}

	@Override
	public List<LeaveMsgInfo> searchLeaveMsgInfoPage(Map<String, Object> param) {
		return mapper.searchLeaveMsgInfoPage(param);
	}

	@Override
	public Integer searchLeaveMsgInfoCount(Map<String, Object> param) {
		return mapper.searchLeaveMsgInfoCount(param);
	}

	@Override
	public int getHandleResult(Integer iid) {
		return mapper.getHandleResult(iid);
	}

	@Override
	public LeaveMsgInfo getById(Integer id) {
		return mapper.getById(id);
	}

	@Override
	public List<LeaveMsgCount> getLeaveMsgInfoCount(List<Integer> userid,
			boolean handled) {
		return mapper.getLeaveMsgInfoCount(handled, userid);
	}

	@Override
	public List<LivechatLeaveMsgStatistics> getLeaveStatistics(Date beginDate,
			Date endDate, String dateFormat,String username) {
		return mapper.getLeaveStatistics(beginDate, endDate, dateFormat,username);
	}

}
