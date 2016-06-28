package dao.manager;

import java.util.List;

import dao.IManagerEnquiryDao;
import entity.manager.ProfessionSkillTopic;

public interface IProfessionSkillTopicEnquiryDao extends IManagerEnquiryDao {
	int getCount();

	int getCount(int skillID);

	ProfessionSkillTopic getByID(int id);

	List<ProfessionSkillTopic> getPage(int p, int size);

	List<ProfessionSkillTopic> getPage(int p, int size, int skillID);

	List<ProfessionSkillTopic> getEnableTopicsByLanguage(int languageID);

	List<ProfessionSkillTopic> getAllEnableTopics();

}
