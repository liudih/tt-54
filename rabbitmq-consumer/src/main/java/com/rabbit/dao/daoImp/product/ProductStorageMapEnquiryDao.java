package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductStorageMapMapper;
import com.rabbit.dao.idao.product.IProductStorageMapEnquiryDao;
import com.rabbit.dto.product.ProductStorageMap;
@Component
public class ProductStorageMapEnquiryDao implements
		IProductStorageMapEnquiryDao {
	@Autowired
	ProductStorageMapMapper productStorageMapMapper;
	@Override
	public void UpdateStatusByStorageIdAndListingId(Boolean available, Integer storageId,
			String listingId) {
		productStorageMapMapper.UpdateStatusByStorageIdAndListingId(available, storageId,
				listingId);
	}

	@Override
	public Integer getProductStorageMapsCountByListingId(String listingId) {
		return productStorageMapMapper
				.getProductStorageMapsCountByListingId(listingId);
	}

	@Override
	public Integer getProductStorageMapsCountByListingIdAndType(
			String listingId, Boolean type) {
		return productStorageMapMapper
				.getProductStorageMapsCountByListingIdAndType(listingId, type);
	}

	@Override
	public List<ProductStorageMap> getProductStorages(List<String> listingIds) {

		return this.productStorageMapMapper.getProductStorages(listingIds);
	}

	
}
