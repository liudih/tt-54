package dao.product;

import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.ProductStorageMap;

public interface IProductStorageMapEnquiryDao extends IProductEnquiryDao {

	List<ProductStorageMap> getProductStorageMapsByListingId(String listingID);

	List<ProductStorageMap> getProductStorages(List<String> listingids);

	Integer getProductStorageMapsCountByListingId(String listingId);

	Integer getProductStorageMapsCountByListingIdAndType(String listingId,
			Boolean type);

	void UpdateStatusByStorageIdAndListingId(Boolean available,
			Integer storageId, String listingId);
}
