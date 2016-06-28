package services.mobile.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import services.IStorageParentService;
import services.IStorageService;
import services.product.IProductEnquiryService;

import com.google.common.collect.Lists;

import dto.Storage;
import dto.StorageParent;
import dto.product.ProductStorageMap;

public class ProductStorageService {

	@Inject
	IProductEnquiryService productEnquiryService;

	@Inject
	IStorageService storageService;

	@Inject
	IStorageParentService storageParentService;

	public List<Map<String, Object>> getProductStorages(String listingId) {
		if (StringUtils.isNotBlank(listingId)) {
			List<ProductStorageMap> storages = productEnquiryService
					.getProductStorageMapsByListingId(listingId);
			if (storages != null && storages.size() > 0) {
				List<Integer> storageIds = Lists.transform(storages, s -> {
					return s.getIstorageid();
				});
				List<Map<String, Object>> result = Lists.newArrayList();
				List<Storage> storageInfos = storageService
						.getStorageByStorageIds(storageIds);
				List<StorageParent> storageParents = storageParentService
						.getAllStorageParentList();
				for (Storage s : storageInfos) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (StorageParent sp : storageParents) {
						if (s.getIparentstorage() == sp.getIid()) {
							map.put("storageid", sp.getIid());
							map.put("name", sp.getCstoragename());
							result.add(map);
						}
					}
				}
				return result;
			}
		}
		return null;
	}
}