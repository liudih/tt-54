package handlers.manager;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.elasticsearch.common.collect.FluentIterable;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import play.Logger;
import services.manager.livechat.LeaveMsgInfoService;
import valueobjects.livechat.leave.LeaveMessage;

import com.google.common.eventbus.Subscribe;

import entity.manager.LeaveMsgInfo;
import extensions.livechat.CustomerServiceFilter;

public class LeaveMessageHandler {
	@Inject
	LeaveMsgInfoService infoService;

	@Inject
	CustomerServiceFilter customerServiceFilter;

	@Subscribe
	public void onPostMessage(LeaveMessage event) throws Exception {
		if (event != null) {
			LeaveMsgInfo l = new LeaveMsgInfo();
			BeanUtils.copyProperties(event, l);
			int topic = 0;
			try {
				topic = Integer.valueOf(l.getCtopic());
				Integer usid = this.assignmentUser(topic, l.getIlanguageid());
				l.setIpretreatmentid(usid);
			} catch (Exception ex) {
				Logger.debug("error-> {} ", ex.getMessage());
			}
			infoService.insert(l);
		}
	}

	private Integer assignmentUser(int topic, int lang) {
		List<Integer> users = customerServiceFilter.getSkillUsers(topic, lang);
		if (users == null || users.size() == 0) {
			Logger.debug("not found assignment User ");
			return null;
		}

		List<valueobjects.manager.LeaveMsgCount> msgcounts = infoService
				.getLeaveMsgInfoCount(users, false);
		if (null == msgcounts || msgcounts.size() == 0) {
			Logger.debug("assignment User {} ", users.get(0));
			return users.get(0);
		}
		Map<Integer, valueobjects.manager.LeaveMsgCount> msgCountMap = Maps
				.uniqueIndex(msgcounts, u -> u.getUserId());

		List<valueobjects.manager.LeaveMsgCount> resultusers = Lists
				.transform(
						users,
						uid -> {
							valueobjects.manager.LeaveMsgCount lmsg = new valueobjects.manager.LeaveMsgCount();
							lmsg.setMsgCount(0);
							if (msgCountMap.containsKey(uid)) {
								lmsg.setMsgCount(msgCountMap.get(uid)
										.getMsgCount());
								Logger.debug(
										"assignment User {} msg count {} ",
										uid, msgCountMap.get(uid).getMsgCount());
							}
							lmsg.setUserId(uid);
							return lmsg;
						});
		List<valueobjects.manager.LeaveMsgCount> resusers = FluentIterable
				.from(resultusers)
				.toSortedList(
						(a, b) -> ((valueobjects.manager.LeaveMsgCount) a)
								.getMsgCount()
								.compareTo(
										((valueobjects.manager.LeaveMsgCount) b)
												.getMsgCount()));
		Logger.debug("assignment User {} ", resusers.get(0).getUserId());
		return resusers.get(0).getUserId();
	}
}
