package dao.product;

import dao.IProductUpdateDao;
import dto.product.ProductExplain;

public interface IProductExplainUpdateDao extends IProductUpdateDao {
	public boolean updateProductExplain(ProductExplain productExplain);

	public boolean addProductExplain(ProductExplain productExplain);

	public boolean deleteByIid(Integer iid);
}
