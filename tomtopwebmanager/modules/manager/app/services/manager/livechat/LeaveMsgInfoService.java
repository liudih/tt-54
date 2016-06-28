package services.manager.livechat;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.BeanUtils;

import play.Logger;
import services.ILanguageService;
import services.customerService.ProfessionSkillService;
import services.manager.AdminUserService;
import session.ISessionService;
import valueobjects.base.Page;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dao.manager.ILeaveMsgInfoEnquiryDao;
import dao.manager.ILeaveMsgInfoUpdateDao;
import dao.manager.IProfessionSkillTopicEnquiryDao;
import entity.manager.AdminUser;
import entity.manager.LeaveMsgInfo;
import entity.manager.ProfessionSkill;
import entity.manager.ProfessionSkillTopic;

public class LeaveMsgInfoService {
	@Inject
	ILeaveMsgInfoUpdateDao updateDao;

	@Inject
	ILeaveMsgInfoEnquiryDao leaveMsgEnquiryDao;
	@Inject
	ILeaveMsgInfoUpdateDao leaveMsgInfoUpdateDao;

	@Inject
	ILanguageService languageService;

	@Inject
	IProfessionSkillTopicEnquiryDao iProfessionSkillTopicEnquiryDao;

	@Inject
	ProfessionSkillService skillService;

	@Inject
	ISessionService sessionService;

	@Inject
	AdminUserService adminUserService;

	final int PAGE_SIZE = 10;

	public boolean insert(LeaveMsgInfo info) {
		Logger.debug("========manager save leave to database=======");
		int i = updateDao.insert(info);
		return 1 == i ? true : false;
	}

	public Page<dto.LeaveMsgInfo> getLeaveMsgInfoPage(int page) {

		List<LeaveMsgInfo> list = leaveMsgEnquiryDao.getLeaveMsgInfoPage(page,
				PAGE_SIZE);

		List<dto.LeaveMsgInfo> voList = this.tranformLeaveMsgList(list);

		int total = leaveMsgEnquiryDao.getCount();

		List<dto.LeaveMsgInfo> leaveList = this.attributeConversion(voList);

		Page<dto.LeaveMsgInfo> p = new Page<dto.LeaveMsgInfo>(leaveList, total,
				page, PAGE_SIZE);
		return p;
	}

	private List<dto.LeaveMsgInfo> attributeConversion(
			List<dto.LeaveMsgInfo> voList) {
		List<dto.SimpleLanguage> languageList = languageService
				.getAllSimpleLanguages();
		Map<Integer, dto.SimpleLanguage> languageMap = Maps.uniqueIndex(
				languageList, new Function<dto.SimpleLanguage, Integer>() {
					public Integer apply(dto.SimpleLanguage lang) {
						return lang.getIid();
					}
				});

		List<ProfessionSkillTopic> topicList = iProfessionSkillTopicEnquiryDao
				.getAllEnableTopics();

		Map<Integer, ProfessionSkillTopic> topicMap = Maps.uniqueIndex(
				topicList, new Function<ProfessionSkillTopic, Integer>() {
					public Integer apply(ProfessionSkillTopic skill) {
						return skill.getIid();
					}
				});

		List<ProfessionSkill> skillList = skillService.getAll();
		Map<Integer, ProfessionSkill> skillMap = Maps.uniqueIndex(skillList,
				new Function<ProfessionSkill, Integer>() {
					public Integer apply(ProfessionSkill skill) {
						return skill.getIid();
					}
				});

		List<dto.LeaveMsgInfo> leaveList = Lists.transform(voList, ab -> {
			Integer langId = ab.getIlanguageid();
			dto.SimpleLanguage lang = languageMap.get(langId);

			if (null == lang) {
				ab.setLanguageName("");
			} else {
				ab.setLanguageName(lang.getCname());
			}

			Integer topicId = 0;
			if (null != ab.getCtopic()) {
				topicId = Integer.parseInt(ab.getCtopic());
			}
			if (null == topicMap.get(topicId)) {
				ab.setCtopic("");
				ab.setSkillName("");
			} else {
				ab.setCtopic(topicMap.get(topicId).getCtitle());
				ab.setSkillName(skillMap.get(
						topicMap.get(topicId).getIskillid()).getCskillname());
			}

			return ab;
		});

		return leaveList;
	}

	public Page<dto.LeaveMsgInfo> searchLeaveMsgInfoPage(int page,
			Integer ilanguageid, Integer itopicid, Boolean bishandle,
			Integer ipretreatmentid, String chandler) {

		Map<String, Object> param = Maps.newHashMap();
		param.put("page", page);
		param.put("pageSize", PAGE_SIZE);
		param.put("ilanguageid", ilanguageid);
		param.put("itopicid", itopicid.toString());
		param.put("bishandle", bishandle);
		param.put("ipretreatmentid", ipretreatmentid);
		param.put("chandler", chandler);
		List<LeaveMsgInfo> list = leaveMsgEnquiryDao
				.searchLeaveMsgInfoPage(param);

		List<dto.LeaveMsgInfo> voList = this.tranformLeaveMsgList(list);
		List<dto.LeaveMsgInfo> leaveList = this.attributeConversion(voList);

		Integer total = leaveMsgEnquiryDao.searchLeaveMsgInfoCount(param);

		Page<dto.LeaveMsgInfo> p = new Page<dto.LeaveMsgInfo>(leaveList, total,
				page, PAGE_SIZE);

		return p;
	}

	private List<dto.LeaveMsgInfo> tranformLeaveMsgList(List<LeaveMsgInfo> list) {

		List<Integer> userids = Lists.newArrayList();
		List<Integer> userids1 = Lists.transform(list,
				obj -> obj.getIpretreatmentid());
		List<Integer> userids2 = Lists.transform(list,
				obj -> obj.getIreplyuserid());
		if (userids1 != null && userids1.size() > 0) {
			userids.addAll(Collections2.filter(userids1, obj -> obj != null));
		}
		if (userids2 != null && userids2.size() > 0) {
			userids.addAll(Collections2.filter(userids2, obj -> obj != null));
		}

		List<AdminUser> auserlist = adminUserService.getUsers(userids);
		Map<Integer, AdminUser> userMap = Maps.uniqueIndex(auserlist,
				obj -> obj.getIid());
		Collection<dto.LeaveMsgInfo> coll = Collections2.transform(
				list,
				msg -> {
					dto.LeaveMsgInfo leaveMsg = new dto.LeaveMsgInfo();
					BeanUtils.copyProperties(msg, leaveMsg);
					if (userMap.containsKey(msg.getIreplyuserid())) {
						leaveMsg.setReplyUserName(userMap.get(
								msg.getIreplyuserid()).getCusername());
					}
					if (userMap.containsKey(msg.getIpretreatmentid())) {
						leaveMsg.setPretreatmentUserName(userMap.get(
								msg.getIpretreatmentid()).getCusername());
					}
					leaveMsg.setReplyContent(msg.getCreplycontent());
					leaveMsg.setReplyDate(msg.getDreplydate());
					return leaveMsg;
				});

		return Lists.newArrayList(coll);
	}

	public boolean leaveMsgInfoHandle(Integer iid) {
		entity.manager.AdminUser user = (entity.manager.AdminUser) sessionService
				.get("ADMIN_LOGIN_CONTEXT");

		// 处理前先判断是否已经有人处理
		int h = this.leaveMsgEnquiryDao.getHandleResult(iid);
		boolean isHandle = h == 0 ? true : false;
		if (isHandle) {
			return false;
		}
		int result = this.leaveMsgInfoUpdateDao.leaveMsgInfoHandle(
				user.getCusername(), iid);
		return result > 0 ? true : false;
	}

	public LeaveMsgInfo getLeaveMsgById(int id) {
		return leaveMsgEnquiryDao.getById(id);
	}

	public int updateById(int replyuserid, String replycontent, int id) {
		return leaveMsgInfoUpdateDao.updateById(replyuserid, replycontent, id);
	}

	public List<valueobjects.manager.LeaveMsgCount> getLeaveMsgInfoCount(
			List<Integer> userid, Boolean handled) {
		return leaveMsgEnquiryDao.getLeaveMsgInfoCount(userid, handled);
	}
}
