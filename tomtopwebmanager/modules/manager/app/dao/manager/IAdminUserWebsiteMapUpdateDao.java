package dao.manager;

import java.util.List;

import dao.IManagerUpdateDao;
import entity.manager.AdminUserWebsitMap;

public interface IAdminUserWebsiteMapUpdateDao extends IManagerUpdateDao {
	public int insert(AdminUserWebsitMap adminUserWebsitMap);

	public int deleteByUserId(Integer userId);
	
	public int insertBatch(List<AdminUserWebsitMap> adminUserWebsitMaps);
}
