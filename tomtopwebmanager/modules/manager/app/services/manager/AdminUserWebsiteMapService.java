package services.manager;

import java.util.List;

import javax.inject.Inject;

import dao.manager.IAdminUserWebsiteMapEnquiryDao;
import dao.manager.IAdminUserWebsiteMapUpdateDao;
import entity.manager.AdminUserWebsitMap;

public class AdminUserWebsiteMapService {
	@Inject
	IAdminUserWebsiteMapEnquiryDao adminUserWebsiteMapEnquiryDao;

	@Inject
	IAdminUserWebsiteMapUpdateDao adminUserWebsitMapUpdateDao;
	
	public List<Integer> getAdminUserWebsitMapsByUserId(Integer userId) {
		return adminUserWebsiteMapEnquiryDao.getWebsiteIdByUserId(userId);
	}

	public boolean deleteByUserId(Integer userId) {
		return adminUserWebsitMapUpdateDao.deleteByUserId(userId) > 0;
	}

	public boolean insertAdminWebsiteMap(AdminUserWebsitMap adminUserWebsitMap) {
		return adminUserWebsitMapUpdateDao.insert(adminUserWebsitMap) > 0;
	}

	public boolean insertBatch(
			List<AdminUserWebsitMap> adminAddminUserWebsiteMaps) {
		return adminUserWebsitMapUpdateDao.insertBatch(adminAddminUserWebsiteMaps) > 0;
	}
}
