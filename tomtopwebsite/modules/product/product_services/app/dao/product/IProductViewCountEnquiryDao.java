package dao.product;

import java.util.List;

import valueobjects.product.ProductViewCount;
import dao.IProductEnquiryDao;

public interface IProductViewCountEnquiryDao extends IProductEnquiryDao {

	List<ProductViewCount> getViewCountListByListingIds(List<String> listingIds);
}
