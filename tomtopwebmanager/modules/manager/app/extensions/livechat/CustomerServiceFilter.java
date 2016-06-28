package extensions.livechat;

import handlers.livechat.LiveChatStatusMonitor;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.elasticsearch.common.collect.FluentIterable;
import org.elasticsearch.common.collect.Maps;

import play.Logger;
import play.libs.F;
import play.libs.Json;
import services.base.FoundationService;
import services.base.SystemParameterService;
import services.customerService.UserSkillTypeEnum;
import services.manager.AdminUserService;
import services.manager.livechat.LiveChatMsgInfoService;
import valueobjects.livechat.Status;
import valueobjects.livechat.session.ChatSession;
import valueobjects.manager.LivechatSessionStatistics;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import dao.manager.ICustomerServiceScheduleEnquiryDao;
import dao.manager.IProfessionSkillDao;
import dao.manager.IProfessionSkillTopicEnquiryDao;
import dao.manager.IUserSkillMapEnquiryDao;
import entity.manager.AdminUser;
import entity.manager.CustomerServiceSchedule;
import entity.manager.ProfessionSkillTopic;
import entity.manager.UserSkillMap;

public class CustomerServiceFilter {

	final int UPPER_LIMIT = 5;
	@Inject
	IUserSkillMapEnquiryDao iUserSkillMapEnquiryDao;

	@Inject
	AdminUserService adminUserService;

	@Inject
	IProfessionSkillDao iProfessionSkillDao;

	@Inject
	IProfessionSkillTopicEnquiryDao iProfessionSkillTopicEnquiryDao;

	@Inject
	ICustomerServiceScheduleEnquiryDao iCustomerServiceScheduleEnquiryDao;

	@Inject
	services.livechat.LiveChatService liveChatService;

	@Inject
	LiveChatStatusMonitor liveChatStatusMonitor;

	@Inject
	LiveChatMsgInfoService infoService;

	@Inject
	SystemParameterService parameterService;

	@Inject
	FoundationService foundation;

	public String getOptimalUser(int topicId, int langageId) {

		// filter only online users
		List<F.Tuple<AdminUser, Status>> onlineUsers = this.getOnlineUsers(
				topicId, langageId);
		if (null == onlineUsers || onlineUsers.isEmpty()) {
			Logger.debug("not found users  ltc ");
			return null;
		}

		// get user related sessions and corresponding LTC
		List<F.Tuple3<AdminUser, Collection<ChatSession>, String>> userSessions = FluentIterable
				.from(onlineUsers)
				.transform(
						(F.Tuple<AdminUser, Status> u) -> F.Tuple3(u._1,
								liveChatService.getChatSessions(u._2.getLtc()),
								u._2.getLtc()))//
				.toList();

		List<String> list = Lists.newArrayList("-1");
		list.addAll(Lists.transform(onlineUsers, us -> us._1.getCusername()));
		List<LivechatSessionStatistics> sessionlist = infoService
				.getUserSessionCount(this.getDate(-1), list);
		Map<String, LivechatSessionStatistics> sessionmap = Maps.uniqueIndex(
				sessionlist, s -> s.getUserName());

		String allowMaxChatNumber = parameterService.getSystemParameter(
				foundation.getSiteID(), foundation.getLanguage(),
				"livechat.allowMaxChatNumber");
		if (null == allowMaxChatNumber || "".equals(allowMaxChatNumber)) {
			allowMaxChatNumber = "5";
		}

		int upperLimit = Integer.parseInt(allowMaxChatNumber);
		Logger.debug("-->count-->{}", Json.toJson(sessionmap));
		// transform to online session count and sorted
		List<F.Tuple3<AdminUser, Integer, String>> sortedUserSessionCount = FluentIterable
				.from(userSessions)
				//
				.transform(us -> F.Tuple3(us._1, //
						(us._2 != null ? us._2.size() : 0), //
						us._3))
				//
				.filter(us -> us._2 < upperLimit)
				.transform(
						us1 -> {
							Integer scount = this.getSessionCount(sessionmap,
									us1._1.getCusername());
							return F.Tuple3(us1._1, (us1._2 + scount), us1._3);
						}).toSortedList((a, b) -> (a._2).compareTo(b._2));

		// get the least session count user and assign to him (if any)
		String resultLTC = FluentIterable.from(sortedUserSessionCount).first()
				.transform(t -> t._3).orNull();
		Logger.debug("----->name-->{}", resultLTC);
		return resultLTC;
	}

	private Integer getSessionCount(
			Map<String, LivechatSessionStatistics> sessionmap, String username) {
		return ((sessionmap != null && sessionmap.containsKey(username) ? sessionmap
				.get(username).getSessoinCount() : 0));
	}

	private Date getDate(int datecount) {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, datecount);
		return calendar.getTime();
	}

	public List<Integer> getUsers(int professionId, int langageId) {

		List<Integer> userlist = this.getScheduleUsers();
		if (userlist == null || userlist.size() <= 0) {
			Logger.debug(" not found schedule user in this time ");
			return null;
		}

		List<UserSkillMap> userprofessionlist = iUserSkillMapEnquiryDao
				.getListByTypeAndSkill(UserSkillTypeEnum.PROFESSION.getValue(),
						professionId);
		if (userprofessionlist == null || userprofessionlist.size() <= 0) {
			Logger.debug(" not found profession user in this time ->"
					+ professionId);
			return null;
		}
		List<Integer> puserids = Lists.transform(userprofessionlist,
				u -> u.getIuserid());
		userlist.retainAll(puserids);
		if (userlist.size() == 0) {
			Logger.debug(" not found schedule and profession user in this time ->");
		}

		List<UserSkillMap> userlanguagelist = iUserSkillMapEnquiryDao
				.getListByTypeAndSkill(UserSkillTypeEnum.LANGUAGE.getValue(),
						langageId);
		if (userlanguagelist == null || userlanguagelist.size() <= 0) {
			Logger.debug(" not found languageid user in this time ->"
					+ langageId);
			return null;
		}
		List<Integer> userids = Lists.transform(userlanguagelist,
				u -> u.getIuserid());
		userlist.retainAll(userids);
		if (userlist.size() == 0) {
			Logger.debug(" not found schedule and profession user in this time ->");
		}

		return userlist;
	}

	public List<Integer> getSkillUsers(int topicId, int langageId) {
		ProfessionSkillTopic topicobj = iProfessionSkillTopicEnquiryDao
				.getByID(topicId);
		if (null == topicobj) {
			Logger.debug(" not found topic ->{} ", topicId);
			return null;
		}
		int professionId = topicobj.getIskillid();
		List<UserSkillMap> userprofessionlist = iUserSkillMapEnquiryDao
				.getListByTypeAndSkill(UserSkillTypeEnum.PROFESSION.getValue(),
						professionId);
		if (userprofessionlist == null || userprofessionlist.size() <= 0) {
			Logger.debug(" not found profession user  ->" + professionId);
			return null;
		}
		List<UserSkillMap> userlanguagelist = iUserSkillMapEnquiryDao
				.getListByTypeAndSkill(UserSkillTypeEnum.LANGUAGE.getValue(),
						langageId);
		if (userlanguagelist == null || userlanguagelist.size() <= 0) {
			Logger.debug(" not found languageid user ->" + langageId);
			return null;
		}
		List<Integer> puserids = Lists.transform(userprofessionlist,
				u -> u.getIuserid());
		List<Integer> userids = Lists.transform(userlanguagelist,
				u -> u.getIuserid());
		puserids.retainAll(userids);
		return puserids;
	}

	private List<Integer> getScheduleUsers() {
		List<CustomerServiceSchedule> users = iCustomerServiceScheduleEnquiryDao
				.getUsers(new Date());
		if (null == users || users.size() == 0) {
			return null;
		}
		return Lists.transform(users, obj -> obj.getIuserid());
	}

	public List<F.Tuple<AdminUser, Status>> getOnlineUsers(int topicId,
			int languageid) {
		ProfessionSkillTopic topicobj = iProfessionSkillTopicEnquiryDao
				.getByID(topicId);
		if (null == topicobj) {
			return null;
		}
		int professionId = topicobj.getIskillid();
		List<Integer> userids = this.getUsers(professionId, languageid);
		if (null == userids || userids.size() == 0) {
			Logger.debug("not found users ");
			return null;
		}

		List<AdminUser> users = adminUserService.getUsers(userids);
		// filter only online users
		List<F.Tuple<AdminUser, Status>> onlineUsers = FluentIterable
				.from(users)
				.transform(
						u -> F.Tuple(u, liveChatStatusMonitor
								.checkStatusByAlias(u.getCusername()))) //
				.filter(t -> t._2 != null) //
				.toList();

		if (onlineUsers == null || onlineUsers.isEmpty()) {
			return null;
		}
		return onlineUsers;
	}

	public Set<String> getScheduleUserNames() {
		List<Integer> userids = this.getScheduleUsers();
		if (userids == null || userids.size() == 0)
			return Sets.newHashSet();
		List<AdminUser> users = adminUserService.getUsers(userids);
		return Sets.newLinkedHashSet(Lists.transform(users,
				u -> u.getCusername()));
	}

	public List<String> getAllScheduleUserName() {
		List<Integer> userids = iCustomerServiceScheduleEnquiryDao
				.getAllScheduleUser();
		if (userids == null || userids.size() == 0)
			return Lists.newArrayList();
		List<AdminUser> users = adminUserService.getUsers(userids);
		return Lists.transform(users, u -> u.getCusername());
	}

}