package services.manager.livechat;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Maps;

import com.google.api.client.util.Lists;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import play.Logger;
import play.libs.Json;
import services.base.utils.DateFormatUtils;
import valueobjects.manager.LivechatSessionStatistics;
import dao.manager.ICustomerServiceScoreEnquiryDao;
import dao.manager.ILeaveMsgInfoEnquiryDao;
import dao.manager.ILivechatMsgInfoEnquiryDao;
import dao.manager.impl.CustomerServiceScoreEnquiryDao;
import dto.LivechatLeaveMsgStatistics;
import dto.LivechatSessionMsgStatistics;
import dto.LivechatSessionScoreStatistics;
import extensions.livechat.CustomerServiceFilter;

public class CustomerServiceStatistics {

	@Inject
	ILivechatMsgInfoEnquiryDao iLivechatMsgInfoEnquiryDao;

	@Inject
	ILeaveMsgInfoEnquiryDao iLeaveMsgInfoEnquiryDao;

	@Inject
	ICustomerServiceScoreEnquiryDao customerServiceScoreEnquiryDao;

	@Inject
	CustomerServiceFilter customerServiceFilter;

	public List<LivechatSessionStatistics> getLivechatStatistics(
			Date beginDate, Date endDate, int calendartype, String username) {

		String dateformat = "yyyy-MM-dd";
		Date tbdate = beginDate;
		Date tedate = endDate;
		if (endDate.after(new Date())) {
			tedate = new Date();
		}
		if (Calendar.MONTH == calendartype) {
			dateformat = "yyyy-MM";
			tbdate = DateFormatUtils.getFormatDateByStr(DateFormatUtils
					.getDate(tbdate, dateformat) + "-01");
		}
		List<String> statisticsData = Lists.newArrayList();

		while (true) {
			if (tbdate.compareTo(tedate) > 0)
				break;
			statisticsData.add(DateFormatUtils.getDate(tbdate, dateformat));
			tbdate = DateFormatUtils.getBeforeDay(tbdate, calendartype, 1);
		}

		List<LivechatSessionMsgStatistics> sesseionmsglist = iLivechatMsgInfoEnquiryDao
				.getSessionStatistics(beginDate, endDate, dateformat, username);
		List<LivechatLeaveMsgStatistics> sessionleavelist = iLeaveMsgInfoEnquiryDao
				.getLeaveStatistics(beginDate, endDate, dateformat, username);
		List<LivechatSessionScoreStatistics> scoreleavelist = customerServiceScoreEnquiryDao
				.getScoreStatistics(beginDate, endDate, dateformat, username);

		Map<String, LivechatSessionMsgStatistics> msgMap = Maps.uniqueIndex(
				sesseionmsglist, sl -> sl.getLatitude() + sl.getUserName());
		Map<String, LivechatLeaveMsgStatistics> leaveMap = Maps.uniqueIndex(
				sessionleavelist, sl -> sl.getLatitude() + sl.getUserName());
		Map<String, LivechatSessionScoreStatistics> scoreMap = Maps
				.uniqueIndex(scoreleavelist,
						sl -> sl.getLatitude() + sl.getUserName());
		Logger.debug("leaveMap  - > {}", Json.toJson(leaveMap));
		List<String> tusernames = customerServiceFilter
				.getAllScheduleUserName();
		if (username != null && username.trim().length() > 0) {
			tusernames = Lists.newArrayList();
			tusernames.add(username);
		}
		List<String> usernames = tusernames;
		List<List<LivechatSessionStatistics>> relist = FluentIterable
				.from(statisticsData)
				.transform(
						t -> {
							List<LivechatSessionStatistics> tl = FluentIterable
									.from(usernames)
									.transform(
											u -> {
												String key = t + u;
												LivechatSessionStatistics lss = new LivechatSessionStatistics();
												lss.setLatitude(t);
												lss.setUserName(u);
												lss.setLevaeMsgCount(0);
												lss.setSessionscroe(0d);
												lss.setSessoinCount(0);
												if (msgMap.containsKey(key)) {
													LivechatSessionMsgStatistics msgsta = msgMap
															.get(key);
													lss.setSessoinCount(msgsta
															.getSessionCount());
													lss.setSetssionTime(msgsta
															.getSessionTime());

												}
												if (leaveMap.containsKey(key)) {
													LivechatLeaveMsgStatistics leaveobj = leaveMap
															.get(key);
													lss.setLevaeMsgCount(leaveobj
															.getLeaveCount());
												}
												if (scoreMap.containsKey(key)) {
													LivechatSessionScoreStatistics lssobj = scoreMap
															.get(key);
													lss.setSessionscroe(lssobj
															.getSessionScore());
												}
												return lss;
											}).toList();
							return tl;
						}).toList();
		List<LivechatSessionStatistics> list = Lists.newArrayList();
		for (List<LivechatSessionStatistics> tlist : relist) {
			list.addAll(tlist);
		}
		// Logger.debug("msg {} -->", Json.toJson(list));
		return FluentIterable.from(list).toSortedList(
				(o1, o2) -> o2.getLatitude().compareTo(o1.getLatitude()));
	}
}
