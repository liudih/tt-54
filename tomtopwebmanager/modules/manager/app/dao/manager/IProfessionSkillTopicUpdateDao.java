package dao.manager;

import dao.IManagerUpdateDao;
import entity.manager.ProfessionSkillTopic;

public interface IProfessionSkillTopicUpdateDao extends IManagerUpdateDao {
	int insert(ProfessionSkillTopic topic);

	int update(ProfessionSkillTopic topic);

}
