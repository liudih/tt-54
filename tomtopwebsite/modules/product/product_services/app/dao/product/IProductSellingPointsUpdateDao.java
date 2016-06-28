package dao.product;

import java.util.List;

import dao.IProductUpdateDao;
import dto.product.ProductSellingPoints;

public interface IProductSellingPointsUpdateDao extends IProductUpdateDao {

	int addBatch(List<ProductSellingPoints> list);

	int deleteByListingId(String listingId);

	int deleteByListingIdAndLanguageId(String listingId, int languageId);

}
