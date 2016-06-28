package dao.product.impl;

import java.util.List;
import java.util.Set;

import mapper.product.ProductLabelMapper;
import play.Logger;
import valueobjects.base.Page;

import com.google.inject.Inject;

import dao.product.IProductLabelEnquiryDao;
import dto.product.ProductLabel;

public class ProductLabelEnquiryDao implements IProductLabelEnquiryDao {
	@Inject
	ProductLabelMapper productLabelMapper;

	@Override
	public List<ProductLabel> getProductLabel(String clistingid) {
		return this.productLabelMapper.getProductLabel(clistingid);
	}

	@Override
	public List<ProductLabel> getBatchProductLabel(List<String> clistingids) {
		return this.productLabelMapper.getBatchProductLabel(clistingids);
	}

	@Override
	public List<ProductLabel> getProductLabelByTypeAndWebsite(
			Integer iwebsiteid, String type) {
		return this.productLabelMapper.getProductLabelByTypeAndWebsite(
				iwebsiteid, type);
	}

	@Override
	public int getProductLabelByListingIdAndTypeAndSite(String listingid,
			Integer websiteId, String type) {
		return this.productLabelMapper
				.getProductLabelByListingIdAndTypeAndSite(listingid, websiteId,
						type);
	}

	@Override
	public List<ProductLabel> getBatchProductLabelByType(
			List<String> clistingids, String type) {
		return this.productLabelMapper.getBatchProductLabelByType(clistingids,
				type);
	}

	@Override
	public List<String> getListingIdByTypeAndWeisiteId(String type,
			Integer iwebsiteid, Integer pagesize, Integer pageNum) {
		return this.productLabelMapper.getListingIdByTypeAndWeisiteId(type,
				iwebsiteid, pagesize, pageNum);
	}

	@Override
	public Set<String> getListingidByTypeAndWebsite(Integer iwebsiteid,
			String type) {
		return this.productLabelMapper.getListingidByTypeAndWebsite(iwebsiteid,
				type);
	}

	// 后台管理
	@Override
	public List<ProductLabel> getListingIdByTypeByPage(String type,
			Integer iwebsiteid, Integer pagesize, Integer pageNum,
			List<String> listingIds) {
		return this.productLabelMapper.getListingIdByTypeByPage(type,
				iwebsiteid, pagesize, pageNum, listingIds);
	}

	@Override
	public int getListingIdByTypeByPageTotalCount(String type, int iwebsiteid,
			List<String> listingIds) {
		return this.productLabelMapper.getListingIdByTypeByPageTotalCount(type,
				iwebsiteid, listingIds);
	}

	@Override
	public ProductLabel getProductLabelById(Integer id) {
		return this.productLabelMapper.getProductLabelById(id);
	}

	@Override
	public int updateProductLable(ProductLabel productLabel) {
		return this.productLabelMapper.updateProductLable(productLabel);
	}

	@Override
	public Page<String> getProductLabelPageByTypeAndWebsite(int siteId, String type, int page, int pageSize) {
		int startIndex = page*pageSize;
		List<String> plListings =  productLabelMapper.getProductLabelPageByTypeAndWebsite(siteId,type,pageSize,startIndex);
		int count = productLabelMapper.getProductLabelCountByTypeAndWebsite(siteId,type);
		return new Page<String>(plListings, count, page, pageSize);
	}

}
