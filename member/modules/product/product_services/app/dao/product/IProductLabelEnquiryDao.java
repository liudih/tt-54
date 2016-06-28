package dao.product;

import java.util.List;
import java.util.Set;

import dao.IProductEnquiryDao;
import dto.product.ProductLabel;
import valueobjects.base.Page;

public interface IProductLabelEnquiryDao extends IProductEnquiryDao {

	List<ProductLabel> getProductLabel(String clistingid);

	List<ProductLabel> getBatchProductLabel(List<String> clistingids);

	List<ProductLabel> getProductLabelByTypeAndWebsite(Integer iwebsiteid,
			String type);

	int getProductLabelByListingIdAndTypeAndSite(String listingid,
			Integer websiteId, String type);

	List<ProductLabel> getBatchProductLabelByType(List<String> clistingids,
			String type);

	List<String> getListingIdByTypeAndWeisiteId(String type,
			Integer iwebsiteid, Integer pagesize, Integer pageNum);

	Set<String> getListingidByTypeAndWebsite(Integer iwebsiteid, String type);

	List<ProductLabel> getListingIdByTypeByPage(String type,
			Integer iwebsiteid, Integer pagesize, Integer pageNum,
			List<String> listingIds);

	int getListingIdByTypeByPageTotalCount(String type, int iwebsiteid,
			List<String> listingIds);

	ProductLabel getProductLabelById(Integer id);

	int updateProductLable(ProductLabel productLabel);
	
	public Page<String> getProductLabelPageByTypeAndWebsite(int siteId, String type, int page, int pageSize);
}
