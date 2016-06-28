package dao.manager;

import java.util.List;

import entity.manager.UserSkillMap;

public interface IUserSkillMapEnquiryDao {
	List<UserSkillMap> getListByUserID(Integer userID);

	List<UserSkillMap> getListByUserIDAndType(Integer userID, String type);

	List<UserSkillMap> getListByTypeAndSkill(String type, Integer skillid);
}
