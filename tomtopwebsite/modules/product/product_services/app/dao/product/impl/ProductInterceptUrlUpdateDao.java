package dao.product.impl;

import javax.inject.Inject;

import mapper.product.ProductInterceptUrlMapper;
import dao.product.IProductInterceptUrlUpdateDao;
import dto.product.ProductInterceptUrl;

public class ProductInterceptUrlUpdateDao implements
		IProductInterceptUrlUpdateDao {

	@Inject
	ProductInterceptUrlMapper interceptUrlMapper;

	@Override
	public int addInterceptUrl(ProductInterceptUrl interceptUrl) {
		return interceptUrlMapper.addProduct(interceptUrl);
	}

}
