package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.AdminUserWebsitMapMapper;
import dao.manager.IAdminUserWebsiteMapUpdateDao;
import entity.manager.AdminUserWebsitMap;

public class AdminUserWebsiteMapUpdateDao implements
		IAdminUserWebsiteMapUpdateDao {
	@Inject
	AdminUserWebsitMapMapper adminUserWebsitMapMapper;

	@Override
	public int insert(AdminUserWebsitMap adminUserWebsitMap) {
		return adminUserWebsitMapMapper.insert(adminUserWebsitMap);
	}

	@Override
	public int deleteByUserId(Integer userId) {
		return adminUserWebsitMapMapper.deleteByUserId(userId);
	}

	@Override
	public int insertBatch(List<AdminUserWebsitMap> adminUserWebsitMaps) {
		return adminUserWebsitMapMapper.insertBatch(adminUserWebsitMaps);
	}

}
