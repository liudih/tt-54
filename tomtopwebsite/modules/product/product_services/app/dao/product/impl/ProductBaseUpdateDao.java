package dao.product.impl;

import mapper.product.ProductBaseMapper;

import com.google.inject.Inject;

import dao.product.IProductBaseUpdateDao;
import dto.product.ProductBase;

public class ProductBaseUpdateDao implements IProductBaseUpdateDao {

	@Inject
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
