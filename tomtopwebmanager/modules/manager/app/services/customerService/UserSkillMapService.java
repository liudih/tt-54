package services.customerService;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import dao.manager.IUserSkillMapEnquiryDao;
import dao.manager.IUserSkillMapUpdateDao;
import entity.manager.UserSkillMap;
import forms.UserSkillEditForm;

public class UserSkillMapService {
	@Inject
	IUserSkillMapEnquiryDao enquiryDao;
	@Inject
	IUserSkillMapUpdateDao updateDao;

	public List<UserSkillMap> getListByUserID(int userID) {
		return enquiryDao.getListByUserID(userID);
	}

	public List<UserSkillMap> getListByUserID(int userID, UserSkillTypeEnum type) {
		return enquiryDao.getListByUserIDAndType(userID, type.getValue());
	}

	public boolean deleteByUserID(int userID) {
		updateDao.deleteByUserID(userID);
		return true;
	}

	public boolean insertList(List<UserSkillMap> list, int userID) {
		int i = 0;
		deleteByUserID(userID);
		for (UserSkillMap map : list) {
			updateDao.insert(map);
			i++;
		}
		if (i == list.size()) {
			return true;
		}
		return false;
	}

	public List<UserSkillMap> convertToList(UserSkillEditForm editForm) {
		List<UserSkillMap> list = Lists.newArrayList();
		list.addAll(convertToList(editForm.getProfessionID(),
				editForm.getUserID(), UserSkillTypeEnum.PROFESSION.getValue()));
		list.addAll(convertToList(editForm.getLanguageID(),
				editForm.getUserID(), UserSkillTypeEnum.LANGUAGE.getValue()));
		return list;
	}

	public Collection<UserSkillMap> convertToList(List<Integer> list,
			int userID, String type) {
		Collection<UserSkillMap> res;
		if (null != list) {
			res = Lists.transform(list, i -> {
				if (null != i) {
					UserSkillMap map = new UserSkillMap();
					map.setCskilltype(type);
					map.setIuserid(userID);
					map.setIskillid(i);
					return map;
				}
				return null;
			});
			res = Collections2.filter(res, e -> null != e);
		} else {
			res = Lists.newArrayList();
		}
		return res;
	}
}
