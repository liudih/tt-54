package com.tomtop.product.services.storage.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tomtop.product.mappers.base.StorageMapper;
import com.tomtop.product.mappers.product.PrdouctStorageMapper;
import com.tomtop.product.models.bo.CategoryStorageBo;
import com.tomtop.product.models.bo.ProductStorageShippingBo;
import com.tomtop.product.models.bo.ShippingMethodBo;
import com.tomtop.product.models.dto.StorageParentDto;
import com.tomtop.product.models.dto.shipping.ShippingMethodDto;
import com.tomtop.product.services.ISearchProductService;
import com.tomtop.product.services.storage.IProductStorageService;
import com.tomtop.product.services.storage.IShippingMethodService;
import com.tomtop.search.entiry.IndexEntity;

@Service
public class ProductStorageServiceImpl implements IProductStorageService {

	@Autowired
	PrdouctStorageMapper productStorageMapper;
	@Autowired
	IShippingMethodService shippingMethodService;
	@Autowired
	StorageMapper storageMapper;
	
	/**
	 * 获取商品仓库及对应的邮寄方式详情
	 * 
	 * @param listingId 
	 * @param qty 数量
	 * @param langId 语言ID
	 * @param siteId 站点
	 * @param currency 货币
	 * @param country 国家简称
	 * 
	 * @return List<ProductStorageShippingBo>
	 */
	@Override
	public List<ProductStorageShippingBo> getProductStorage(String listingId,Integer qty, Integer langId,
			Integer siteId, String currency,String country) {
		List<ProductStorageShippingBo> psboList = new ArrayList<ProductStorageShippingBo>();
		//获取商品有在哪些仓库中
		List<Integer> storageIdList = productStorageMapper.getProductStorageByListingId(listingId);
		if(storageIdList == null || storageIdList.size() == 0){
			return psboList;
		}
		List<StorageParentDto> spdList = storageMapper.getStorage(storageIdList);
		
		if(spdList == null || spdList.size() == 0){
			return psboList;
		}
		List<ShippingMethodBo> smbolist = null;
		Integer storageId = null;
		String storageName = null;
		ProductStorageShippingBo pssbo = null;
		for (int i = 0; i < spdList.size(); i++) {
			storageId = spdList.get(i).getIid();
			storageName = spdList.get(i).getCstoragename();
			ShippingMethodDto requst = shippingMethodService.getShippingMethodDto(
					storageId, listingId, qty, country, langId, currency, siteId);
			smbolist = shippingMethodService.getShippingMethodInformations(requst);
			if(smbolist != null && smbolist.size() > 0){
				pssbo = new ProductStorageShippingBo();
				pssbo.setStorageId(storageId);
				pssbo.setStorageName(storageName);
				pssbo.setShippingMethods(smbolist);
				psboList.add(pssbo);
			}
		}
		return psboList;
	}

	/**
	 * 获取所有仓库信息
	 * 
	 * 
	 * @return List<CategoryStorageBo>
	 */
	@Override
	public List<CategoryStorageBo> getAllCategoryStorageBo() {
		List<CategoryStorageBo> csboList = new ArrayList<CategoryStorageBo>();
		
		List<StorageParentDto> spdtoList = storageMapper.getStorageParentAll();
		StorageParentDto spdto = null;
		CategoryStorageBo csbo = null;
		Integer id = 0;
		String name = "";
		for (int i = 0; i < spdtoList.size(); i++) {
			spdto = spdtoList.get(i);
			id = spdto.getIid();
			name = spdto.getCstoragename();
			csbo = new CategoryStorageBo();
			csbo.setId(id);
			csbo.setName(name);
			csboList.add(csbo);
		}
		return csboList;
	}
	
	@Autowired
	ISearchProductService searchProductService;
	
	/**
	 * 根据仓库获取邮寄方式详情
	 * 
	 * @param storage
	 * @param listingId 
	 * @param qty 数量
	 * @param langId 语言ID
	 * @param siteId 站点
	 * @param currency 货币
	 * @param country 国家简称
	 * 
	 * @return List<ShippingMethodBo>
	 */
	@Override
	public List<ShippingMethodBo> getStoragesShippingMethod(Integer storageId,
			String listingId,Integer qty, Integer langId,
			Integer siteId, String currency,String country) {
		IndexEntity indexEntity = searchProductService.getSearchProduct(listingId, langId, siteId);
		ShippingMethodDto ssmdto = shippingMethodService.getStorageShippingMethodDto(storageId, listingId, qty, country, langId, currency, siteId, indexEntity);
		List<ShippingMethodBo> smboList = shippingMethodService.getStorageShippingMethodInformations(ssmdto);
		return smboList;
	}
}
