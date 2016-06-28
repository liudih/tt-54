package dao.product;

import java.util.List;

import dao.IProductUpdateDao;
import dto.product.ProductVideo;

public interface IProductVideoUpdateDao extends IProductUpdateDao {

	public int addProductVideoList(List<ProductVideo> list);
	
	public int deleteByListingId(String listingId);
}
