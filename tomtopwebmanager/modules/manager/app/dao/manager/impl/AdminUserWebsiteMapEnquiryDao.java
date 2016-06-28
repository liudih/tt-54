package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.manager.AdminUserWebsitMapMapper;
import dao.manager.IAdminUserWebsiteMapEnquiryDao;

public class AdminUserWebsiteMapEnquiryDao implements
		IAdminUserWebsiteMapEnquiryDao {
	@Inject
	AdminUserWebsitMapMapper adminUserWebsitMapMapper;

	@Override
	public List<Integer> getWebsiteIdByUserId(Integer userId) {
		return adminUserWebsitMapMapper.getWebsiteIdByUserId(userId);
	}
}
