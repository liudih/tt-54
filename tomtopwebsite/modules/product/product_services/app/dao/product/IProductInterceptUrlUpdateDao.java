package dao.product;

import dao.IProductUpdateDao;
import dto.product.ProductInterceptUrl;

public interface IProductInterceptUrlUpdateDao extends IProductUpdateDao {

	public int addInterceptUrl(ProductInterceptUrl interceptUrl);

}
