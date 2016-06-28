package controllers.manager.livechat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.base.utils.DateFormatUtils;
import services.livechat.LiveChatService;
import services.manager.AdminUserService;
import services.manager.livechat.CustomerServiceStatistics;
import services.manager.livechat.LiveChatMsgInfoService;
import valueobjects.base.Page;
import valueobjects.livechat.session.ChatSession;
import valueobjects.manager.LivechatSessionStatistics;
import valueobjects.manager.search.HistoryMsgContext;

import com.google.api.client.util.Lists;
import com.google.common.collect.FluentIterable;

import dto.HistoryMessageForm;
import entity.manager.LivechatMsgInfo;

public class LiveChat extends Controller {
	@Inject
	LiveChatMsgInfoService infoService;

	@Inject
	CustomerServiceStatistics customerServiceStatistics;

	@Inject
	LiveChatService liveChatService;

	@Inject
	AdminUserService adminUserService;

	@controllers.AdminRole(menuName = "LiveChatHistory")
	public Result searchHistory(String sessionId) {
		Form<HistoryMessageForm> form = Form.form(HistoryMessageForm.class)
				.bindFromRequest();

		HistoryMsgContext context = new HistoryMsgContext();
		HistoryMessageForm uform = form.get();

		BeanUtils.copyProperties(uform, context);

		if (null != sessionId) {
			ChatSession session = liveChatService.getChatSession(sessionId);
			if (null != session) {
				valueobjects.livechat.Status status = liveChatService
						.getStatusByLTC(session.getOrigin());
				context.setCustomerName(status.getAlias());
				context.setCustomerLtc(session.getOrigin());
				uform.setCustomerName(status.getAlias());
			}
		}
		Page<LivechatMsgInfo> p = infoService.searchHistoryMsg(context);

		return ok(views.html.manager.livechat.history.render(p, uform));
	}

	@controllers.AdminRole(menuName = "LiveChatHistory")
	public Result searchHistoryMsgPage(int page, String customerName,
			String customerServiceName, String keyword, String startDate,
			String endDate) {

		Page<LivechatMsgInfo> p = infoService.searchHistoryMsgPage(page,
				customerName, customerServiceName, keyword, startDate, endDate);

		HistoryMessageForm uform = new HistoryMessageForm();
		uform.setCustomerName(customerName);
		uform.setCustomerServiceName(customerServiceName);
		uform.setEndDate(DateFormatUtils.getFormatDateByStr(endDate));
		uform.setStartDate(DateFormatUtils.getFormatDateByStr(startDate));
		uform.setKeyword(keyword);

		return ok(views.html.manager.livechat.history.render(p, uform));
	}

	public Result getSessionStatistics() {

		DynamicForm in = Form.form().bindFromRequest();
		// in.data().containsKey(key)
		String sdate = in.get("startDate");
		String edate = in.get("endDate");
		String calendartype = in.get("ctype");
		String csidStr = in.get("customerservice");
		Integer csid = -1;
		List<dto.AdminUser> auserList = adminUserService.getAllAdminUser();
		List<LivechatSessionStatistics> relist = Lists.newArrayList();
		if (null == sdate || null == edate) {
			return ok(views.html.manager.livechat.customerservice_statistics
					.render(relist, DateFormatUtils.getNowBeforeByDay(
							Calendar.MONTH, -1), new Date(), Calendar.DATE,
							auserList, csid));
		}
		int ctype = Calendar.MONTH;// 2
		if (String.valueOf(Calendar.DATE).equals(calendartype)) {// 5
			ctype = Calendar.DATE;
		}
		try {
			csid = Integer.valueOf(csidStr);
		} catch (Exception ex) {
			csid = -1;
		}
		final Integer uid = csid;
		dto.AdminUser auser = FluentIterable.from(auserList)
				.filter(o -> o.getIid() == uid).first().orNull();
		String username = null;
		if (null != auser) {
			csid = auser.getIid();
			username = auser.getCusername();
		}
		Date beginDate = DateFormatUtils.getFormatDateByStr(sdate);
		Date endDate = DateFormatUtils.getFormatDateByStr(edate);
		Logger.debug("stardate {}  enddate {} ", beginDate, endDate);
		if (null == beginDate || null == endDate) {
			return badRequest(" start / end date invalid! ");
		}
		relist = customerServiceStatistics.getLivechatStatistics(beginDate,
				endDate, ctype, username);

		return ok(views.html.manager.livechat.customerservice_statistics
				.render(relist, beginDate, endDate, ctype, auserList, csid));
	}
}
