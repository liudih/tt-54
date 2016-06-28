package dao.product;

import java.util.List;

import dao.IProductUpdateDao;
import dto.product.ProductStorageMap;

public interface IProductStorageMapUpdateDao extends IProductUpdateDao {

	int addProductStorageList(List<ProductStorageMap> list);

	int deleteByListingId(String listingId);

	int deleteProductStorageList(String listingId, List<Integer> storageIds);

}
