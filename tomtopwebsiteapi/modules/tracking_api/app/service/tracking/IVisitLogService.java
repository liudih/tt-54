package service.tracking;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IVisitLogService {

	public abstract Integer saveVisitLog(int siteID, String aid, String source,
			String ip, String path, String eid, Integer taskType);

	public abstract List<dto.VisitLog> getVisitLogByDateRange(String caid,
			Date begindate, Date enddate);

	public abstract List<dto.VisitLog> getVisitLogLimitByParamMap(
			Map<String, Object> queryParamMap);

	public abstract int getVisitLogCountByParamMap(
			Map<String, Object> queryParamMap);

	public abstract List<dto.VisitLog> getVisitLogByParamMap(
			Map<String, Object> queryParamMap);

	public abstract List<dto.VisitLog> getVisitLogByDateRange(Date begindate,
			Date enddate);

	public abstract List<dto.VisitLog> getVisitLogByAids(List<String> aids,
			Date begindate, Date enddate);
	
	public int  getVisitLogByDateRangeCount(String caid,
			Date begindate, Date enddate);
}