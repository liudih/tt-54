package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductSellingPoints;

public interface IProductSellingPointsEnquiryDao extends IProductEnquiryDao {

	List<ProductSellingPoints> getProductSellingPointsByListingIdAndLanguage(
			String listingID, int lang);

	List<ProductSellingPoints> getProductSellingPointsByListingIds(
			List<String> listingids, Integer lang);

	List<ProductSellingPoints> getProductSellingPointsByListingId(
			String listingID);

	List<ProductSellingPoints> getProductSellingPointsByListingIdsOnly(
			List<String> listingIDs);
}
