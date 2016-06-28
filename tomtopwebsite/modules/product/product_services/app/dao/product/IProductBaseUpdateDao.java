package dao.product;

import dao.IProductUpdateDao;
import dto.product.ProductBase;

public interface IProductBaseUpdateDao extends IProductUpdateDao {
	public int updateQtyByListing(int qty, String listingId);

	public int updateFreightBySkuAndSite(String sku, int websiteId,
			double freight);

	public int updateStatusBySkuAndSite(String sku, int websiteId,
			Integer status);

	public int updateSpuBySkuAndSite(String sku, int websiteId, String spu);

	public int deleteByListing(String listing);

	public int addByProduct(ProductBase productBase);

	public int updateProductMainAndVisible(String clistingid, Boolean main,
			Boolean visible);

	public int updateProductByListingid( boolean visible,
			String listingid);

}
