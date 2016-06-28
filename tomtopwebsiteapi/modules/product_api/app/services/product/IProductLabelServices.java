package services.product;

import java.util.List;
import java.util.Set;

import context.WebContext;
import dto.product.ProductLabel;

public interface IProductLabelServices {

	Set<String> getListingsByType(String type);

	List<String> getListByListingIdsAndType(List<String> listingIds, String type);

	List<String> getListingIdByTypeAndWeisiteId(String type,
			WebContext context, Integer pagesize, Integer pageNum);

	List<ProductLabel> getProductLabel(String clistingid);

	Boolean getProductLabelByListingIdAndTypeAndSite(String listingid,
			WebContext context, String type);
}
