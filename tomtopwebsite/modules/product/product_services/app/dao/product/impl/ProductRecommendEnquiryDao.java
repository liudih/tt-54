package dao.product.impl;

import java.util.List;

import mapper.product.ProductRecommendMapper;

import com.google.inject.Inject;

import dao.product.IProductRecommendEnquiryDao;
import dto.product.ProductRecommend;

public class ProductRecommendEnquiryDao implements IProductRecommendEnquiryDao {

	@Inject
	ProductRecommendMapper productRecommendMapper;

	@Override
	public List<ProductRecommend> getProductRecommendsByListingId(
			String clistingid) {
		return this.productRecommendMapper
				.getProductRecommendsByListingId(clistingid);
	}

	@Override
	public int deleteAllRecommendProduct() {
		return this.productRecommendMapper.deleteAllRecommendProduct();
	}
}
