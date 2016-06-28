package dao.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

import dao.IManagerEnquiryDao;
import dto.LivechatLeaveMsgStatistics;
import entity.manager.LeaveMsgInfo;

public interface ILeaveMsgInfoEnquiryDao extends IManagerEnquiryDao {
	List<LeaveMsgInfo> getLeaveMsgInfoPage(int page, int pageSize);

	int getCount();

	List<LeaveMsgInfo> searchLeaveMsgInfoPage(Map<String, Object> param);

	Integer searchLeaveMsgInfoCount(Map<String, Object> param);

	int getHandleResult(Integer iid);

	LeaveMsgInfo getById(Integer id);

	List<valueobjects.manager.LeaveMsgCount> getLeaveMsgInfoCount(
			List<Integer> userid, boolean handled);

	List<LivechatLeaveMsgStatistics> getLeaveStatistics(Date beginDate,
			Date endDate, String dateFormat,String username);

}
