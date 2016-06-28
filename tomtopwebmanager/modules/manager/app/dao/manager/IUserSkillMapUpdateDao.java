package dao.manager;

import entity.manager.UserSkillMap;

public interface IUserSkillMapUpdateDao {
	int insert(UserSkillMap map);

	int deleteByUserID(Integer userID);
}
