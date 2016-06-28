package dao.product.impl;

import mapper.product.ProductParentUrlMapper;

import com.google.inject.Inject;

import dao.product.IProductParentUrlUpdateDao;
import dto.product.ProductParentUrl;

public class ProductParentUrlUpdateDao implements IProductParentUrlUpdateDao {

	@Inject
	ProductParentUrlMapper productParentUrlMapper;

	@Override
	public int addProductParentUrl(ProductParentUrl record) {
		return this.productParentUrlMapper.addProductParentUrl(record);
	}

}
