package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductStorageMapMapper;
import dao.product.IProductStorageMapUpdateDao;
import dto.product.ProductStorageMap;

public class ProductStorageMapUpdateDao implements IProductStorageMapUpdateDao {

	@Inject
	ProductStorageMapMapper productStorageMapMapper;
	
	@Override
	public int addProductStorageList(List<ProductStorageMap> list) {
		return this.productStorageMapMapper.addProductStorageList(list);
	}

	@Override
	public int deleteByListingId(String listingId) {
		return this.productStorageMapMapper.deleteByListingId(listingId);
	}

	@Override
	public int deleteProductStorageList(String listingId, List<Integer> storageIds) {
		return this.productStorageMapMapper.deleteProductStorageList(listingId, storageIds);
	}


}
