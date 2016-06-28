package dao.product;

import dao.IProductUpdateDao;

public interface IProductViewCountUpdateDao extends IProductUpdateDao {

	int alterViewCount(int siteId, String listingId);

	void addViewCount(int siteID, String listingId, int initialCount);
}
