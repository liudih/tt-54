package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductExplainType;

public interface IProductExplainTypeEnquiryDao extends IProductEnquiryDao {
	public List<ProductExplainType> getAllExplainType();
}
