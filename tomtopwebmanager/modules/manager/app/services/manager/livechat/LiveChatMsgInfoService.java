package services.manager.livechat;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.annotations.Param;

import play.Logger;
import services.base.utils.DateFormatUtils;

import valueobjects.base.Page;
import valueobjects.manager.LivechatSessionStatistics;
import valueobjects.manager.search.HistoryMsgContext;
import dao.manager.ILivechatMsgInfoEnquiryDao;
import dao.manager.ILivechatMsgInfoUpdateDao;
import entity.manager.LivechatMsgInfo;

public class LiveChatMsgInfoService {
	@Inject
	ILivechatMsgInfoUpdateDao updateDao;
	@Inject
	ILivechatMsgInfoEnquiryDao iLivechatMsgInfoEnquiryDao;

	final static int PAGE_SIZE = 10;

	public boolean insert(LivechatMsgInfo info) {
		int i = updateDao.insert(info);
		return 1 == i ? true : false;
	}

	public Page<LivechatMsgInfo> searchHistoryMsg(HistoryMsgContext context) {
		List<LivechatMsgInfo> msgInfoList = updateDao.searchHistoryMsg(context);

		Integer total = updateDao.searchHistoryMsgCount(context);
		Page<LivechatMsgInfo> p = new Page<LivechatMsgInfo>(msgInfoList, total,
				context.getPage(), PAGE_SIZE);

		return p;
	}

	public Page<LivechatMsgInfo> searchHistoryMsgPage(int page,
			String customerName, String customerServiceName, String keyword,
			String startDate, String endDate) {
		HistoryMsgContext context = new HistoryMsgContext();
		context.setPage(page);
		context.setCustomerName(customerName);
		context.setCustomerServiceName(customerServiceName);
		if (null != endDate && !"".equals(endDate)) {

			Date eDate = DateFormatUtils.getFormatDateYmdhmsByStr(endDate
					+ " 23:59:59");
			context.setEndDate(eDate);
		}
		if (null != startDate && !"".equals(startDate)) {

			Date sDate = DateFormatUtils.getFormatDateYmdhmsByStr(startDate
					+ " 00:00:00");
			context.setStartDate(sDate);
		}
		context.setKeyword(keyword);
		context.setPage(page);

		List<LivechatMsgInfo> msgInfoList = updateDao.searchHistoryMsg(context);
		Integer total = updateDao.searchHistoryMsgCount(context);
		Logger.debug(total + "============result===============");
		Page<LivechatMsgInfo> p = new Page<LivechatMsgInfo>(msgInfoList, total,
				context.getPage(), PAGE_SIZE);

		return p;
	}

	public List<LivechatSessionStatistics> getUserSessionCount(Date fromDate,
			@Param("list") List<String> users) {
		return iLivechatMsgInfoEnquiryDao.getUserSessionCount(fromDate, users);
	}
}
