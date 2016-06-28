package dao.product;

import java.util.List;

import dao.IProductUpdateDao;
import dto.product.ProductUrl;

public interface IProductUrlUpdateDao extends IProductUpdateDao {
	public int addProductUrl(ProductUrl record);

	public int addProductUrlList(List<ProductUrl> list);

	public int deleteByListingId(String listingId);
}
