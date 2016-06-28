package services.product;

import java.util.List;

import javax.inject.Inject;

import dao.product.IProductExplainTypeEnquiryDao;
import dto.product.ProductExplainType;

public class ProductExplainTypeService {
	@Inject
	IProductExplainTypeEnquiryDao productExplainTypeEnquityDao;

	public List<ProductExplainType> getAllExplainType() {
		return productExplainTypeEnquityDao.getAllExplainType();
	}

}
