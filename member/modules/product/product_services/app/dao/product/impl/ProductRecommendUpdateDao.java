package dao.product.impl;

import mapper.product.ProductRecommendMapper;

import com.google.inject.Inject;

import dao.product.IProductRecommendUpdateDao;

public class ProductRecommendUpdateDao implements IProductRecommendUpdateDao {

	@Inject
	ProductRecommendMapper productRecommendMapper;

	@Override
	public int insert(String clistinid, String crecommendlisting,
			String ccreateuser) {
		return this.productRecommendMapper.add(clistinid, crecommendlisting,
				ccreateuser);
	}

}
