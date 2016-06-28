package dao.product;

import dao.IProductUpdateDao;
import dto.product.ProductBundleSale;

public interface IProductBundleSaleUpdateDao extends IProductUpdateDao {
	public int deleteAuto();
	
	public int alterAutoBundleSaleVisible();
	
	int activeBundle(String listing,String bundListing);
	
	int insert(ProductBundleSale record);
	
}
