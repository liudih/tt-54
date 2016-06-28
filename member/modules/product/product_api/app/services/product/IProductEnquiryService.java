package services.product;

import java.util.List;

import valueobjects.product.ProductLite;

import com.website.dto.product.Product;

import context.WebContext;
import dto.product.ProductBase;
import dto.product.ProductSalePrice;

public interface IProductEnquiryService {

	List<Product> getProductsByListingIds(List<String> listingids);

	ProductBase getBaseByListingIdAndLanguage(String listingId,
			Integer languageId);

	ProductBase getBaseByListingId(String listingId);

	String getListingIdByParentSkuAndWebsiteIdAndStatusAndIsMain(
			String parentsku, Integer isstatus, Integer websiteId,
			boolean ismain);

	List<ProductBase> getProductsWithSameParentSkuMatchingAttributes(
			String listingID, WebContext context);

	List<String> selectListingidBySearchNameAndSort(WebContext context,
			String searchname, String sort, Integer categoryId,
			List<String> pcListingIds);

	/**
	 * 获取描述外的信息
	 * 
	 * @param listingId
	 * @param languageId
	 * @return
	 */
	ProductBase getProductByListingIdAndLanguageWithdoutDesc(String listingId,
			Integer languageId);

	/**
	 * 分段获取描述，描述信息量大一次获取不全
	 * 
	 * @param listingId
	 * @param languageId
	 * @param begin
	 * @param len
	 * @return
	 */
	String getProductDescByListingIdAndLanguagePart(String listingId,
			Integer languageId, int begin, int len);

	/**
	 * @param listingids
	 * @return
	 */
	List<ProductBase> getProductBasesByListingIds(List<String> listingids);
	
	public List<ProductLite> getProductLiteByListingIDs(
			List<String> listingIDs, int websiteID, int languageID);

	/**
	 * 
	 * @Title: getProductBaseBySkus
	 * @Description: TODO(通过SKU列表查询产品列表)
	 * @param @param skus
	 * @param @param siteid
	 * @param @return
	 * @return List<ProductBase>
	 * @throws 
	 * @author yinfei
	 */
	public List<ProductBase> getProductBaseBySkus(List<String> skus,
			Integer siteid);
	
	public int getCountBundleProduct(String main,String bundle);
}
