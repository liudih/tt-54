package com.rabbit.dao.idao.product;

import java.util.List;

import com.rabbit.dao.idao.IProductUpdateDao;
import com.rabbit.dto.product.ProductStorageMap;

public interface IProductStorageMapUpdateDao extends IProductUpdateDao {

	int addProductStorageList(List<ProductStorageMap> list);

	int deleteByListingId(String listingId);

	int deleteProductStorageList(String listingId, List<Integer> storageIds);

}
