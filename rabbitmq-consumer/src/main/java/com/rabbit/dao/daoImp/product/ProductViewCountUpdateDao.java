package com.rabbit.dao.daoImp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductViewCountMapper;
import com.rabbit.dao.idao.product.IProductViewCountUpdateDao;
@Component
public class ProductViewCountUpdateDao implements IProductViewCountUpdateDao {

	@Autowired
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
