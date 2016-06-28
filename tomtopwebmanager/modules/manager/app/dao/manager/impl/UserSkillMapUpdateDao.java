package dao.manager.impl;

import javax.inject.Inject;

import mapper.manager.UserSkillMapMapper;
import dao.manager.IUserSkillMapUpdateDao;
import entity.manager.UserSkillMap;

public class UserSkillMapUpdateDao implements IUserSkillMapUpdateDao {
	@Inject
	UserSkillMapMapper mapper;

	@Override
	public int insert(UserSkillMap map) {
		return mapper.insert(map);
	}

	@Override
	public int deleteByUserID(Integer userID) {
		return mapper.deleteByUserID(userID);
	}
}
