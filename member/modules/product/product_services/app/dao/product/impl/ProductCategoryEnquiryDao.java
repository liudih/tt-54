package dao.product.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import mapper.product.CategoryNameMapper;
import mapper.product.ProductCategoryMapperMapper;
import valueobjects.product.category.CategoryThreeLevel;
import dao.product.IProductCategoryEnquiryDao;
import dto.product.CategoryName;

public class ProductCategoryEnquiryDao implements IProductCategoryEnquiryDao {

	@Inject
	ProductCategoryMapperMapper productCategoryMapperMapper;

	@Inject
	CategoryNameMapper categoryNameMapper;

	@Override
	public Integer getProductRootCategoryIdByListingId(String listingId,
			Integer languageId) {
		return productCategoryMapperMapper.getProductRootCategoryIdByListingId(
				listingId, languageId);
	}

	@Override
	public List<String> getAllListingIdsByRootId(Integer rootCategoryId) {
		return productCategoryMapperMapper
				.getAllListingIdsByRootId(rootCategoryId);
	}

	@Override
	public CategoryName getCategoryNameByCategoryIdAndLanguage(
			Integer rootCategoryId, Integer languageId) {
		return categoryNameMapper.getCategoryNameByCategoryIdAndLanguage(
				rootCategoryId, languageId);
	}

	@Override
	public List<String> getAllListingIdsByRootIds(List<Integer> rootCategoryIds) {
		return productCategoryMapperMapper
				.getAllListingIdsByRootIds(rootCategoryIds);
	}

	@Override
	public List<String> getListingIdsByRootId(Integer rootCategoryId,
			Integer pageSize, Integer pageNum) {
		return productCategoryMapperMapper.getListingIdsByRootId(
				rootCategoryId, pageSize, pageNum);
	}

	@Override
	public List<String> getListingIds(List<Integer> rootCategoryId,
			Integer pageSize, Integer pageNum) {
		return productCategoryMapperMapper.getListingIdsByCategoryId(
				rootCategoryId, pageSize, pageNum);
	}

	@Override
	public List<CategoryThreeLevel> getProductThreeLevelCategoryBySku(
			List<String> csku, Date dcreatestartdate, Date dcreateenddate,
			Integer ilangid) {
		// TODO Auto-generated method stub
		return productCategoryMapperMapper.getProductThreeLevelCategoryBySku(
				csku, dcreatestartdate, dcreateenddate, ilangid);
	}
}
