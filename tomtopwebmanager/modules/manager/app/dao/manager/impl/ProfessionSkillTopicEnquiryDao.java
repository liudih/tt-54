package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.ProfessionSkillTopicMapper;
import dao.manager.IProfessionSkillTopicEnquiryDao;
import entity.manager.ProfessionSkillTopic;

public class ProfessionSkillTopicEnquiryDao implements
		IProfessionSkillTopicEnquiryDao {
	@Inject
	ProfessionSkillTopicMapper mapper;

	@Override
	public int getCount() {
		return mapper.getCount();
	}

	@Override
	public ProfessionSkillTopic getByID(int id) {
		return mapper.getByID(id);
	}

	@Override
	public List<ProfessionSkillTopic> getPage(int p, int size) {
		return mapper.getPage(p, size);
	}

	@Override
	public List<ProfessionSkillTopic> getPage(int p, int size, int skillID) {
		return mapper.searchPage(p, size, skillID);
	}

	@Override
	public List<ProfessionSkillTopic> getEnableTopicsByLanguage(int languageID) {
		return mapper.getEnableTopicsByLanguage(languageID);
	}

	@Override
	public int getCount(int skillID) {
		return mapper.searchCount(skillID);
	}
	
	@Override
	public List<ProfessionSkillTopic> getAllEnableTopics() {
		return mapper.getAllEnableTopics();
	}
}
