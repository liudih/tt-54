package services.product;

import java.util.List;

import dto.product.ProductVideo;

public interface IProductVideoEnquiryService {
	
	public List<ProductVideo> getVideosBylistId(String clistingid);

}
