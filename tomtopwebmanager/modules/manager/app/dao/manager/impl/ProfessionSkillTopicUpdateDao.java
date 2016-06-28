package dao.manager.impl;

import javax.inject.Inject;

import mapper.manager.ProfessionSkillTopicMapper;
import dao.manager.IProfessionSkillTopicUpdateDao;
import entity.manager.ProfessionSkillTopic;

public class ProfessionSkillTopicUpdateDao implements
		IProfessionSkillTopicUpdateDao {
	@Inject
	ProfessionSkillTopicMapper mapper;

	@Override
	public int insert(ProfessionSkillTopic topic) {
		return mapper.insert(topic);
	}

	@Override
	public int update(ProfessionSkillTopic topic) {
		return mapper.update(topic);
	}

}
