package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductRecommend;

public interface IProductRecommendEnquiryDao extends IProductEnquiryDao {

	List<ProductRecommend> getProductRecommendsByListingId(String clistingid);

	int deleteAllRecommendProduct();
}
