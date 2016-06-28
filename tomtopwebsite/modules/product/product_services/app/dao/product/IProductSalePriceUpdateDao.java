package dao.product;

import java.util.Date;
import java.util.List;

import dao.IProductUpdateDao;
import dto.product.ProductSalePrice;

public interface IProductSalePriceUpdateDao extends IProductUpdateDao {

	int addProductSalePrice(ProductSalePrice record);

	int updateByPrimaryKeySelective(ProductSalePrice record);

	int addBatch(List<ProductSalePrice> list);
	
	int deleteProductCurrentSalePrice(String listingId);
	
	int deleteProductSalePriceByDate(String listingId,
			Date start, Date end);
}
