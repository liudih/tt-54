package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductStorageMapMapper;
import dao.product.IProductStorageMapEnquiryDao;
import dto.product.ProductStorageMap;

public class ProductStorageMapEnquiryDao implements
		IProductStorageMapEnquiryDao {

	@Inject
	ProductStorageMapMapper productStorageMapMapper;

	@Override
	public List<ProductStorageMap> getProductStorageMapsByListingId(
			String listingId) {
		return this.productStorageMapMapper
				.getProductStorageMapsByListingId(listingId);
	}

	@Override
	public List<ProductStorageMap> getProductStorages(List<String> listingIds) {

		return this.productStorageMapMapper.getProductStorages(listingIds);
	}

	@Override
	public Integer getProductStorageMapsCountByListingIdAndType(
			String listingId, Boolean type) {
		return productStorageMapMapper
				.getProductStorageMapsCountByListingIdAndType(listingId, type);
	}

	@Override
	public Integer getProductStorageMapsCountByListingId(String listingId) {
		return productStorageMapMapper
				.getProductStorageMapsCountByListingId(listingId);
	}

	@Override
	public void UpdateStatusByStorageIdAndListingId(Boolean available, Integer storageId,
			String listingId) {
		productStorageMapMapper.UpdateStatusByStorageIdAndListingId(available, storageId,
				listingId);
	}

}
