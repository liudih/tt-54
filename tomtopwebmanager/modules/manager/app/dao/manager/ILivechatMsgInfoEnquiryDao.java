package dao.manager;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import dto.LivechatSessionMsgStatistics;
import valueobjects.manager.LivechatSessionStatistics;

public interface ILivechatMsgInfoEnquiryDao {
	List<LivechatSessionStatistics> getUserSessionCount(Date fromDate,
			@Param("list")List<String> users);
	
	List<LivechatSessionMsgStatistics> getSessionStatistics(Date beginDate,
			Date endDate, String dateFormat,String username);
}
