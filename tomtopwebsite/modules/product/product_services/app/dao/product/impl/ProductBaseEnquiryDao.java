package dao.product.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import mapper.product.ProductBaseMapper;

import com.google.api.client.util.Lists;
import com.google.inject.Inject;

import dao.product.IProductBaseEnquiryDao;
import dto.product.ProductBase;

public class ProductBaseEnquiryDao implements IProductBaseEnquiryDao {
	@Inject
	ProductBaseMapper productBaseMapper;

	@Override
	public List<String> getLisingByStatus(int status) {
		return productBaseMapper.getLisingsByStatus(status);
	}

	@Override
	public List<ProductBase> getProductBaseBySkuAndWebsiteId(String csku,
			int siteID) {
		return productBaseMapper.getProductBaseBySkuAndWebsiteId(csku, siteID);
	}

	@Override
	public ProductBase getProductBaseByListingId(String listing) {
		return productBaseMapper.getProductBaseByListingId(listing);
	}

	@Override
	public ProductBase getProductByWebsiteIdAndListingId(int websiteid,
			String clistingid) {
		return productBaseMapper.getProductByWebsiteAndListingid(websiteid,
				clistingid);
	}

	@Override
	public int getProductQtyByListingID(String listingID) {
		Integer qty = productBaseMapper.getQtyByListingID(listingID);
		return qty != null ? qty : 0;
	}

	@Override
	public List<String> getChildrenListingIdByParentSku(String parentSku) {
		return productBaseMapper.getChildrenListingIdByParentSku(parentSku);
	}

	@Override
	public Integer updateProductMainAndVisible(String clistingid, Boolean main,
			Boolean visible) {
		return productBaseMapper.updateProductMainAndVisible(clistingid, main,
				visible);
	}

	/*
	 * @Override public void setNewMainProduct(String listingId, String
	 * parentSku) { productBaseMapper.setProductBmainFalse(listingId,parentSku);
	 * productBaseMapper.setProductBmainTrue(listingId,parentSku); }
	 */

	@Override
	public List<ProductBase> getRelatedSkuByClistingid(String listingid) {
		return productBaseMapper.getRelatedSkuByClistingid(listingid);
	}

	@Override
	public List<ProductBase> getRelatedSkuByListingids(List<String> clistingids) {
		return productBaseMapper.getProductByListingIds(clistingids);
	}

	@Override
	public List<ProductBase> getClistingIdBySpu(String spu) {
		return productBaseMapper.getClistingidBySpu(spu);
	}

	@Override
	public String getProductSkuByListingId(String listingId) {
		return productBaseMapper.getProductSkuByListingId(listingId);
	}

	@Override
	public ProductBase getProductListingId(String sku, Integer siteId,
			Integer status, Boolean bvisible, Boolean bactivity) {
		return productBaseMapper.getProductListingId(sku, siteId, status,
				bvisible, bactivity);
	}

	@Override
	public ProductBase getProductByWebsiteIdAndListingIdAndStatus(
			int websiteid, String listingid) {
		return null;
	}

	@Override
	public List<String> getLisingIdsByDate(List<Integer> listingids,
			Date startDate, Date endDate) {
		return productBaseMapper.getLisingIdsByDate(listingids, startDate,
				endDate);
	}

	@Override
	public Set<String> getListingsByCanShow(Integer siteId) {
		return productBaseMapper.getListingsByCanShow(siteId);
	}

	@Override
	public String getListingsBySku(String sku,Integer siteId) {
		return productBaseMapper.getListingsBySku(sku,siteId);
	}

	@Override
	public List<ProductBase> getProductBaseBySkus(List<String> skus,
			Integer siteid) {
		if(skus.size() == 0){
			return Lists.newArrayList();
		}else{
			return productBaseMapper.getProductBaseBySkus(skus, siteid);
		}
	}

	@Override
	public List<String> getSkuBySpu(String spu) {
		return productBaseMapper.getSkuBySpu(spu);
	}

	@Override
	public String getSkuByListingid(String listingid) {
		return productBaseMapper.getSkuByListingid(listingid);
	}

	@Override
	public String getListingidBySku(String sku) {
		return productBaseMapper.getListingidBySku(sku);
	}

	@Override
	public ProductBase getProductBySku(String sku, Integer siteId, Integer state) {
		return productBaseMapper.getProductBySku(sku, siteId, state);
	}

	@Override
	public ProductBase getProductByCskuAndIsActivity(String csku,Integer siteId) {
		return productBaseMapper.getProductByCskuAndIsActivity(csku,siteId);
	}

	@Override
	public ProductBase getBasePriceBySku(String mainSku) {
		return productBaseMapper.getBasePriceBySku(mainSku);
	}

	@Override
	public List<String> getListingIdsBySpus(List<String> cspus, Integer websiteId) {
		return productBaseMapper.getlistingIdsBySpus(cspus, websiteId);
	}

	@Override
	public ProductBase getStatusByListingIdAndsiteId(String listingid, Integer siteId) {
		return productBaseMapper.getStatusByListingIdAndsiteId(listingid, siteId);
	}

}
