package services.product;

import java.util.List;

import javax.inject.Inject;

import dao.product.IProductVideoEnquiryDao;
import dto.product.ProductVideo;

public class ProductVideoEnquiryService implements IProductVideoEnquiryService{

	@Inject
	IProductVideoEnquiryDao productVideoEnquiryDao;
	
	@Override
	public List<ProductVideo> getVideosBylistId(String clistingid) {
		return productVideoEnquiryDao.getVideosBylistId(clistingid);
	}

}
