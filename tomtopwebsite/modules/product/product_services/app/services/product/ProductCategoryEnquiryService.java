package services.product;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.category.CategoryProductRecommendMapper;
import valueobjects.product.category.CategoryThreeLevel;
import dao.product.IProductCategoryEnquiryDao;

public class ProductCategoryEnquiryService implements
		IProductCategoryEnquiryService {
	@Inject
	IProductCategoryEnquiryDao dao;
	@Inject
	CategoryProductRecommendMapper cprMapper;

	@Override
	public List<String> getAllListingIdsByRootId(Integer rootCategoryId) {
		return dao.getAllListingIdsByRootId(rootCategoryId);
	}

	@Override
	public List<String> getListingIdsByRootId(Integer rootCategoryId,
			Integer pageSize, Integer pageNum) {
		return dao.getListingIdsByRootId(rootCategoryId, pageSize, pageNum);
	}

	@Override
	public List<String> getListingIds(List<Integer> rootCategoryId,
			Integer pageSize, Integer pageNum) {
		return dao.getListingIds(rootCategoryId, pageSize, pageNum);
	}

	@Override
	public List<String> getRecommendListingid() {
		return cprMapper.searchRecommendListingidByUpddate();
	}

	@Override
	public List<String> getRecommendListingidAll() {
		return cprMapper.searchRecommendListingidAll();
	}

	@Override
	public List<CategoryThreeLevel> getProductThreeLevelCategoryBySku(
			List<String> cskus, Date dcreatestartdate, Date dcreateenddate,
			Integer ilangid) {
		// TODO Auto-generated method stub
		return dao.getProductThreeLevelCategoryBySku(cskus, dcreatestartdate,
				dcreateenddate, ilangid);
	}
}
