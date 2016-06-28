package services.customerService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;

import play.Logger;
import services.ILanguageService;
import services.manager.AdminUserService;
import valueobjects.base.Page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import dao.manager.IProfessionSkillTopicEnquiryDao;
import dao.manager.IProfessionSkillTopicUpdateDao;
import dto.AdminUser;
import dto.ProfessionSkillTopicDTO;
import dto.SimpleLanguage;
import entity.manager.ProfessionSkill;
import entity.manager.ProfessionSkillTopic;
import forms.ProfessionSkillTopicForm;

public class ProfessionSkillTopicService {
	@Inject
	IProfessionSkillTopicUpdateDao updateDao;
	@Inject
	IProfessionSkillTopicEnquiryDao enquiryDao;
	@Inject
	AdminUserService userService;
	@Inject
	ProfessionSkillService skillService;
	@Inject
	ILanguageService languageService;
	@Inject
	ObjectMapper objectMapper;

	private int size = 15;

	public int getCount() {
		return enquiryDao.getCount();
	}

	public ProfessionSkillTopic getByID(int id) {
		return enquiryDao.getByID(id);
	}

	public Page<ProfessionSkillTopicDTO> getPage(int p) {
		List<ProfessionSkillTopic> list = enquiryDao.getPage(p, size);
		int total = enquiryDao.getCount();
		return getPage(list, total, p);
	}

	private Page<ProfessionSkillTopicDTO> getPage(
			List<ProfessionSkillTopic> list, int total, int p) {
		List<SimpleLanguage> langList = languageService.getAllSimpleLanguages();
		Map<Integer, SimpleLanguage> langMap = Maps.uniqueIndex(langList,
				l -> l.getIid());
		List<AdminUser> userList = userService.getadminUserList();
		Map<Integer, AdminUser> userMap = Maps.uniqueIndex(userList,
				u -> u.getIid());
		List<ProfessionSkill> skillList = skillService.getAll();
		Map<Integer, ProfessionSkill> skillMap = Maps.uniqueIndex(skillList,
				s -> s.getIid());
		List<ProfessionSkillTopicDTO> dtoList = Lists
				.transform(
						list,
						t -> {
							ProfessionSkillTopicDTO dto = objectMapper
									.convertValue(t,
											ProfessionSkillTopicDTO.class);
							dto.setUserName(userMap.get(dto.getIcreateuser()) != null ? userMap
									.get(dto.getIcreateuser()).getCusername()
									: null);
							dto.setSkillName(skillMap.get(dto.getIskillid()) != null ? skillMap
									.get(dto.getIskillid()).getCskillname()
									: null);
							dto.setLanguageName(langMap.get(dto
									.getIlanguageid()) != null ? langMap.get(
									dto.getIlanguageid()).getCname() : null);
							return dto;
						});
		dtoList = Lists.newArrayList(Collections2.filter(dtoList,
				e -> null != e));
		return new Page<ProfessionSkillTopicDTO>(dtoList, total, p, size);
	}

	public Page<ProfessionSkillTopicDTO> getPage(int p, int skillID) {
		List<ProfessionSkillTopic> list = enquiryDao.getPage(p, size, skillID);
		int total = enquiryDao.getCount(skillID);
		return getPage(list, total, p);
	}

	public List<ProfessionSkillTopic> getEnableTopicsByLanguage(int languageID) {
		return enquiryDao.getEnableTopicsByLanguage(languageID);
	}

	public boolean insert(ProfessionSkillTopic topic) {
		int i = updateDao.insert(topic);
		return 1 == i ? true : false;
	}

	public boolean update(ProfessionSkillTopic topic) {
		int i = updateDao.update(topic);
		return 1 == i ? true : false;
	}

	public boolean insert(ProfessionSkillTopicForm addForm,
			entity.manager.AdminUser user) {
		ProfessionSkillTopic topic = new ProfessionSkillTopic();
		try {
			BeanUtils.copyProperties(topic, addForm);
		} catch (IllegalAccessException e) {
			Logger.error("insert: ", e);
		} catch (InvocationTargetException e) {
			Logger.error("insert: ", e);
		}
		topic.setIcreateuser(user.getIid());
		return insert(topic);
	}

	public boolean update(ProfessionSkillTopicForm saveForm) {
		ProfessionSkillTopic topic = new ProfessionSkillTopic();
		try {
			BeanUtils.copyProperties(topic, saveForm);
		} catch (IllegalAccessException e) {
			Logger.error("insert: ", e);
		} catch (InvocationTargetException e) {
			Logger.error("insert: ", e);
		}
		return update(topic);
	}
	
	public List<ProfessionSkillTopic> getAllEnableTopics() {
		return enquiryDao.getAllEnableTopics();
	}
}
