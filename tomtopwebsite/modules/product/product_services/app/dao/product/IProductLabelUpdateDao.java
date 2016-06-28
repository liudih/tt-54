package dao.product;

import java.util.List;

import dao.IProductUpdateDao;
import dto.product.ProductLabel;

public interface IProductLabelUpdateDao extends IProductUpdateDao {
	int insert(ProductLabel productLabel);

	int insertOrUpdate(ProductLabel productLabel);

	int deleteBySiteAndType(int site, String type);

	int deleteByListingId(String listingid);

	void batchInsert(List<ProductLabel> productLabels);

	void deleteProductLabelByType(Integer iwebsiteid, String type);

	int deleteByListingAndType(String listingid, String type);

	int deleteByListingIdAndWebsiteId(String listingId, Integer websiteId);
	
	int deleteByListingIdsAndType(List<String> listingids, String type);
	
	boolean isExists(String listingid,String type);
}
