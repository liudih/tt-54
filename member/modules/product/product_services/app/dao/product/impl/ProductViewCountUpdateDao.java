package dao.product.impl;

import mapper.product.ProductViewCountMapper;

import com.google.inject.Inject;

import dao.product.IProductViewCountUpdateDao;

public class ProductViewCountUpdateDao implements IProductViewCountUpdateDao {

	@Inject
	ProductViewCountMapper productViewCountMapper;

	@Override
	public int alterViewCount(int siteId, String listingId) {

		return this.productViewCountMapper.alterViewCount(siteId, listingId);
	}

	@Override
	public void addViewCount(int siteID, String listingID, int initialCount) {
		this.productViewCountMapper.addViewCount(siteID, listingID,
				initialCount);
	}

}
