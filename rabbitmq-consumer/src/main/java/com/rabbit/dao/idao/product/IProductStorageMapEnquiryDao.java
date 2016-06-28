package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductEnquiryDao;
import com.rabbit.dto.product.ProductStorageMap;


public interface IProductStorageMapEnquiryDao extends IProductEnquiryDao {
	void UpdateStatusByStorageIdAndListingId(Boolean available,
			Integer storageId, String listingId);
	Integer getProductStorageMapsCountByListingId(String listingId);
	
	Integer getProductStorageMapsCountByListingIdAndType(String listingId,
			Boolean type);
	List<ProductStorageMap> getProductStorages(List<String> listingids);
}
