package services.product;

import java.util.List;

import dto.product.ProductRecommend;

public interface IProductRecommendService {

	int deleteAllRecommendProduct();

	List<ProductRecommend> getProductRecommendsByListingId(String clistingid);

}
