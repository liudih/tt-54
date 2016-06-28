package com.rabbit.dao.idao.product;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.rabbit.dao.idao.IProductEnquiryDao;
import com.rabbit.dto.product.ProductBase;

public interface IProductBaseEnquiryDao extends IProductEnquiryDao {
	public List<String> getLisingByStatus(int status);

	public List<ProductBase> getProductBaseBySkuAndWebsiteId(String csku,
			int siteID);

	public ProductBase getProductBaseByListingId(String listing);

	public ProductBase getProductByWebsiteIdAndListingId(int websiteid,
			String listingid);

	public int getProductQtyByListingID(String listingID);

	public List<String> getChildrenListingIdByParentSku(String parentSku);

	public Integer updateProductMainAndVisible(String clistingid, Boolean main,
			Boolean visible);

	public List<ProductBase> getRelatedSkuByClistingid(String clistingid);

	public List<ProductBase> getRelatedSkuByListingids(List<String> clistingids);

	public List<ProductBase> getClistingIdBySpu(String spu);

	public ProductBase getProductByWebsiteIdAndListingIdAndStatus(
			int websiteid, String listingid);

	public String getProductSkuByListingId(String listingId);

	public ProductBase getProductListingId(String sku, Integer siteId,
			Integer status, Boolean bvisible, Boolean bactivity);

	public List<String> getLisingIdsByDate(List<Integer> listingids,
			Date startDate, Date endDate);

	public Set<String> getListingsByCanShow(Integer siteId);

	public String getListingsBySku(String sku,Integer siteId);

	public List<ProductBase> getProductBaseBySkus(List<String> skus,
			Integer siteid);

	public List<String> getSkuBySpu(String spu);

	public String getSkuByListingid(String listingid);

	public String getListingidBySku(String sku);

	public ProductBase getProductBySku(String sku, Integer siteId, Integer state);

	public ProductBase getProductByCskuAndIsActivity(String csku,Integer siteId);

	public ProductBase getBasePriceBySku(String mainSku);

	public List<String> getListingIdsBySpus(List<String> cspus, Integer websiteId);
	
	public ProductBase getStatusByListingIdAndsiteId(String listingid,Integer siteId);
}
