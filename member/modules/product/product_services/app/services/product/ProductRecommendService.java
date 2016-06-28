package services.product;

import java.util.List;

import com.google.inject.Inject;

import dao.product.IProductRecommendEnquiryDao;
import dto.product.ProductRecommend;

public class ProductRecommendService implements IProductRecommendService {

	@Inject
	IProductRecommendEnquiryDao productRecommendEnquiryDao;

	@Override
	public List<ProductRecommend> getProductRecommendsByListingId(
			String clistingid) {
		return productRecommendEnquiryDao
				.getProductRecommendsByListingId(clistingid);
	}

	@Override
	public int deleteAllRecommendProduct() {
		return productRecommendEnquiryDao.deleteAllRecommendProduct();
	}

}
