package dao.product;

import dao.IProductUpdateDao;
import dto.product.ProductParentUrl;

public interface IProductParentUrlUpdateDao extends IProductUpdateDao {
	int addProductParentUrl(ProductParentUrl record);
}
