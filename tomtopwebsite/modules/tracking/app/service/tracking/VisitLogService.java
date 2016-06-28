package service.tracking;

import interceptors.CacheResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import play.Logger;
import mappers.tracking.VisitLogMapper;
import services.base.utils.DateFormatUtils;

import com.google.common.collect.Lists;

import entity.tracking.VisitLog;

public class VisitLogService implements IVisitLogService {

	@Inject
	VisitLogMapper visitLogMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.tracking.IVisitLogService#saveVisitLog(int,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String, java.lang.Integer)
	 */
	public Integer saveVisitLog(int siteID, String aid, String source,
			String ip, String path, String eid, Integer taskType) {
		VisitLog visitLog = new VisitLog();
		visitLog.setCaid(aid);
		visitLog.setCip(ip);
		visitLog.setCpath(path);
		visitLog.setCsource(source);
		visitLog.setIwebsiteid(siteID);
		visitLog.setCeid(eid);
		visitLog.setItasktype(taskType);
		visitLog.setDcreateDate(DateFormatUtils.getCurrentTimeD());
		try {
			return visitLogMapper.insert(visitLog);
		} catch (Exception ex) {
			Logger.error("save visilog error: ", ex);
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * service.tracking.IVisitLogService#getVisitLogByDateRange(java.lang.String
	 * , java.util.Date, java.util.Date)
	 */
	public List<dto.VisitLog> getVisitLogByDateRange(String caid,
			Date begindate, Date enddate) {

		List<VisitLog> logs = visitLogMapper.getVisitLogsByAid(caid, begindate,
				enddate);

		return Lists.transform(logs, g -> {
			dto.VisitLog v = new dto.VisitLog();
			v.setCaid(g.getCaid());
			v.setCip(g.getCip());
			v.setCpath(g.getCpath());
			v.setCsource(g.getCsource());
			v.setDcreatedate(g.getDcreateDate());
			v.setIwebsiteid(g.getIwebsiteid());
			return v;
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * service.tracking.IVisitLogService#getVisitLogLimitByParamMap(java.util
	 * .Map)
	 */
	public List<dto.VisitLog> getVisitLogLimitByParamMap(
			Map<String, Object> queryParamMap) {

		List<VisitLog> logs = visitLogMapper
				.getVisitLogLimitByParamMap(queryParamMap);

		return Lists.transform(logs, g -> {
			dto.VisitLog v = new dto.VisitLog();
			v.setCaid(g.getCaid());
			v.setCip(g.getCip());
			v.setCpath(g.getCpath());
			v.setCsource(g.getCsource());
			v.setDcreatedate(g.getDcreateDate());
			v.setIwebsiteid(g.getIwebsiteid());
			return v;
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * service.tracking.IVisitLogService#getVisitLogCountByParamMap(java.util
	 * .Map)
	 */
	public int getVisitLogCountByParamMap(Map<String, Object> queryParamMap) {
		return visitLogMapper.getVisitLogCountByParamMap(queryParamMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * service.tracking.IVisitLogService#getVisitLogByParamMap(java.util.Map)
	 */
	public List<dto.VisitLog> getVisitLogByParamMap(
			Map<String, Object> queryParamMap) {
		List<VisitLog> logs = visitLogMapper
				.getVisitLogByParamMap(queryParamMap);

		return Lists.transform(logs, g -> {
			dto.VisitLog v = new dto.VisitLog();
			v.setCaid(g.getCaid());
			v.setCip(g.getCip());
			v.setCpath(g.getCpath());
			v.setCsource(g.getCsource());
			v.setDcreatedate(g.getDcreateDate());
			v.setIwebsiteid(g.getIwebsiteid());
			return v;
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * service.tracking.IVisitLogService#getVisitLogByDateRange(java.util.Date,
	 * java.util.Date)
	 */
	public List<dto.VisitLog> getVisitLogByDateRange(Date begindate,
			Date enddate) {
		List<VisitLog> logs = visitLogMapper.getVisitLogsByDateRange(begindate,
				enddate);

		return Lists.transform(logs, g -> {
			dto.VisitLog v = new dto.VisitLog();
			v.setCaid(g.getCaid());
			v.setCip(g.getCip());
			v.setCpath(g.getCpath());
			v.setCsource(g.getCsource());
			v.setDcreatedate(g.getDcreateDate());
			v.setIwebsiteid(g.getIwebsiteid());
			return v;
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.tracking.IVisitLogService#getVisitLogByAids(java.util.List,
	 * java.util.Date, java.util.Date)
	 */
	public List<dto.VisitLog> getVisitLogByAids(List<String> aids,
			Date begindate, Date enddate) {
		List<VisitLog> logs = visitLogMapper.getVisitLogByAids(aids, begindate,
				enddate, 0);

		return Lists.transform(logs, g -> {
			dto.VisitLog v = new dto.VisitLog();
			v.setCaid(g.getCaid());
			v.setCip(g.getCip());
			v.setCpath(g.getCpath());
			v.setCsource(g.getCsource());
			v.setDcreatedate(g.getDcreateDate());
			v.setIwebsiteid(g.getIwebsiteid());
			return v;
		});
	}

	@Override
	@CacheResult("tracking")
	public int getVisitLogByDateRangeCount(String caid, Date begindate,
			Date enddate) {
		return visitLogMapper.getVisitLogsByAidCount(caid, begindate, enddate);
	}
}
