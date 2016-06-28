package com.rabbit.dao.daoImp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductBaseMapper;
import com.rabbit.dao.idao.product.IProductBaseUpdateDao;
import com.rabbit.dto.product.ProductBase;
@Component
public class ProductBaseUpdateDao implements IProductBaseUpdateDao {

	@Autowired
	ProductBaseMapper productBaseMapper;

	@Override
	public int updateQtyByListing(int qty, String listingId) {
		return productBaseMapper.updateQtyByListing(qty, listingId);
	}

	@Override
	public int updateFreightBySkuAndSite(String sku, int websiteId,
			double freight) {
		return productBaseMapper.updateFreightBySkuAndSite(sku, websiteId,
				freight);
	}

	@Override
	public int updateStatusBySkuAndSite(String sku, int websiteId,
			Integer status) {
		return productBaseMapper.updateStatusBySkuAndSite(sku, websiteId,
				status);
	}

	@Override
	public int updateSpuBySkuAndSite(String sku, int websiteId, String spu) {
		return productBaseMapper.updateSpuBySkuAndSite(sku, websiteId, spu);
	}

	@Override
	public int deleteByListing(String listing) {
		return productBaseMapper.deleteByListing(listing);
	}

	@Override
	public int addByProduct(ProductBase productBase) {
		return productBaseMapper.insert(productBase);
	}

	@Override
	public int updateProductMainAndVisible(String clistingid, Boolean main,
			Boolean visible) {
		return productBaseMapper.updateProductMainAndVisible(clistingid, main,
				visible);
	}
	
	@Override
	public int updateProductByListingid(boolean visible,String listingid){
		return productBaseMapper.updateProductByListingid(visible, listingid);
	}

}
