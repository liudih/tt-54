package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.UserSkillMapMapper;
import dao.manager.IUserSkillMapEnquiryDao;
import entity.manager.UserSkillMap;

public class UserSkillMapEnquiryDao implements IUserSkillMapEnquiryDao {
	@Inject
	UserSkillMapMapper mapper;

	@Override
	public List<UserSkillMap> getListByUserID(Integer userID) {
		return mapper.getListByUserID(userID);
	}

	@Override
	public List<UserSkillMap> getListByUserIDAndType(Integer userID, String type) {
		return mapper.getListByUserIDAndType(userID, type);
	}

	@Override
	public List<UserSkillMap> getListByTypeAndSkill(String type, Integer skillid) {
		return mapper.getListByTypeAndSkill(type, skillid);
	}

}
