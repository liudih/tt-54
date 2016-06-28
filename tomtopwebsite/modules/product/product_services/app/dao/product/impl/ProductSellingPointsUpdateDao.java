package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductSellingPointsMapper;
import dao.product.IProductSellingPointsUpdateDao;
import dto.product.ProductSellingPoints;

public class ProductSellingPointsUpdateDao implements
		IProductSellingPointsUpdateDao {

	@Inject
	ProductSellingPointsMapper productSellingPointsMapper;

	@Override
	public int addBatch(List<ProductSellingPoints> list) {
		return this.productSellingPointsMapper.addBatch(list);
	}

	@Override
	public int deleteByListingId(String listingId) {
		return this.productSellingPointsMapper.deleteByListingId(listingId);
	}

	@Override
	public int deleteByListingIdAndLanguageId(String listingId, int languageId) {
		return this.productSellingPointsMapper.deleteByListingIdAndLanguage(
				listingId, languageId);
	}

}
