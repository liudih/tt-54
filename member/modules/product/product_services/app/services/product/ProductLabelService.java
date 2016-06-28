package services.product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import services.base.FoundationService;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import context.WebContext;
import dao.product.IProductLabelEnquiryDao;
import dao.product.IProductLabelUpdateDao;
import dto.product.ProductLabel;

public class ProductLabelService implements IProductLabelServices {

	@Inject
	FoundationService foundationService;

	@Inject
	IProductLabelUpdateDao productLabelUpdateDao;

	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;

	@Override
	public Set<String> getListingsByType(String type) {
		List<ProductLabel> productLabels = this.productLabelEnquiryDao
				.getProductLabelByTypeAndWebsite(foundationService.getSiteID(),
						type);
		Set<String> listings = new HashSet<String>();
		listings.addAll(Lists.transform(productLabels, e -> e.getClistingid()));
		return listings;
	}

	public Boolean getProductLabelByListingIdAndTypeAndSite(String listingid,
			Integer websiteId, String type) {
		boolean result = true;
		int count = this.productLabelEnquiryDao
				.getProductLabelByListingIdAndTypeAndSite(listingid, websiteId,
						type);
		if (count < 1) {
			result = false;
		}
		return result;
	}

	@Override
	public Boolean getProductLabelByListingIdAndTypeAndSite(String listingid,
			WebContext context, String type) {
		Integer websiteId = foundationService.getSiteID(context);
		return this.getProductLabelByListingIdAndTypeAndSite(listingid,
				websiteId, type);
	}

	public boolean addProductLabel(ProductLabel productLabel) {
		int result = this.productLabelUpdateDao.insertOrUpdate(productLabel);
		return result > 0 ? true : false;
	}

	public boolean deleteProductLabelByListingIdAndSiteId(String listingId,
			Integer websiteId) {
		int result = this.productLabelUpdateDao.deleteByListingId(listingId);
		return result > 0 ? true : false;
	}

	@Override
	public List<String> getListByListingIdsAndType(List<String> listingIds,
			String type) {
		List<ProductLabel> labels = this.productLabelEnquiryDao
				.getBatchProductLabelByType(listingIds, type);
		return Lists.transform(labels, e -> e.getClistingid());
	}

	public List<String> getListingIdByTypeAndWeisiteId(String type,
			Integer iwebsiteid, Integer pagesize, Integer pageNum) {
		return this.productLabelEnquiryDao.getListingIdByTypeAndWeisiteId(type,
				iwebsiteid, pagesize, pageNum);
	}

	@Override
	public List<String> getListingIdByTypeAndWeisiteId(String type,
			WebContext context, Integer pagesize, Integer pageNum) {
		Integer iwebsiteid = foundationService.getSiteID(context);
		return this.getListingIdByTypeAndWeisiteId(type, iwebsiteid, pagesize,
				pageNum);
	}

	/**
	 * 通过clistingid来查询Label
	 * 
	 * @param clistingid
	 * @return
	 */
	@Override
	public List<ProductLabel> getProductLabel(String clistingid) {
		return this.productLabelEnquiryDao.getProductLabel(clistingid);
	}

	/**
	 * @param type
	 * @param iwebsiteid
	 * @param pagesize
	 * @param pageNum
	 * @param listingIds
	 * @return List<ProductLabel>
	 */
	public List<ProductLabel> getListingIdByTypeByPage(String type,
			Integer iwebsiteid, Integer pagesize, Integer pageNum,
			List<String> listingIds) {
		return this.productLabelEnquiryDao.getListingIdByTypeByPage(type,
				iwebsiteid, pagesize, pageNum, listingIds);

	}

	/**
	 * @param type
	 * @param listingIds
	 * @param siteId
	 * @return int toalCount
	 */
	public int getListingIdByTypeByPageTotalCount(String type, int iwebsiteid,
			List<String> listingIds) {
		return this.productLabelEnquiryDao.getListingIdByTypeByPageTotalCount(
				type, iwebsiteid, listingIds);
	}

	public boolean updateProductLable(ProductLabel productLabel) {
		int result = productLabelEnquiryDao.updateProductLable(productLabel);
		return result > 0 ? true : false;
	}

	public ProductLabel getProductLabelById(Integer id) {
		return productLabelEnquiryDao.getProductLabelById(id);
	}
}
