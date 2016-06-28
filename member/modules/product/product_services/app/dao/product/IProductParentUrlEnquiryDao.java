package dao.product;

import dao.IProductEnquiryDao;
import dto.product.ProductParentUrl;

public interface IProductParentUrlEnquiryDao extends IProductEnquiryDao {
	
	ProductParentUrl getProductParentUrlByUrlAndLanguageId(String url,
			Integer languageId);
}
