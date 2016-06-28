package dao.product;

import java.util.List;

import dto.product.ProductImage;

public interface IProductImageDao {

	List<ProductImage> getProductImageByListingIds(List<String> listingids);

	
	
	
}
