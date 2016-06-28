package services.product;

import java.util.List;

import mapper.product.ProductCategoryRankMapper;

import com.google.inject.Inject;

import dto.product.ProductCategoryRank;

public class RankEnquiryService {
	@Inject
	ProductCategoryRankMapper productCategoryRankMapper;

	private Integer size = 10;

	public List<ProductCategoryRank> getRank(Integer categoryIds, Integer languageId) {
		return null;
	}
}
