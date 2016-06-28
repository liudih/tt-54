package services.product;

import java.util.List;

import context.WebContext;
import dto.product.ProductSellingPoints;

public interface IProductSellingPointsService {

	List<ProductSellingPoints> getProductSellingPointsByListingIdAndLanguage(
			String listingID, WebContext context);

	List<ProductSellingPoints> getProductSellingPointsByListingIds(
			List<String> listingids, WebContext context);

	List<ProductSellingPoints> getProductSellingPointsByListingId(
			String listingID);

	List<ProductSellingPoints> getProductSellingPointsByListingIdsOnly(
			List<String> listingIDs);

}
