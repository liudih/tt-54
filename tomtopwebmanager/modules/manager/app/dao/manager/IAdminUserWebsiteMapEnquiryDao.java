package dao.manager;

import java.util.List;

import dao.IManagerEnquiryDao;

public interface IAdminUserWebsiteMapEnquiryDao extends IManagerEnquiryDao {
	public List<Integer> getWebsiteIdByUserId(Integer userId);
}
