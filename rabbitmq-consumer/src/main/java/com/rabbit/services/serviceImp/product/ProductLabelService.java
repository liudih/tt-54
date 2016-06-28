package com.rabbit.services.serviceImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.rabbit.dao.idao.product.IProductLabelEnquiryDao;
import com.rabbit.dao.idao.product.IProductLabelServices;
import com.rabbit.dao.idao.product.IProductLabelUpdateDao;
import com.rabbit.dto.product.ProductLabel;
@Service
public class ProductLabelService implements IProductLabelServices {

	@Autowired
	IProductLabelUpdateDao productLabelUpdateDao;

	@Autowired
	IProductLabelEnquiryDao productLabelEnquiryDao;


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
