package com.rabbit.dao.idao.product;

import com.rabbit.dao.idao.IProductUpdateDao;

public interface IProductViewCountUpdateDao extends IProductUpdateDao {

	int alterViewCount(int siteId, String listingId);

	void addViewCount(int siteID, String listingId, int initialCount);
}
