package dao.product.impl;

import mapper.product.ProductBundleSaleMapper;

import com.google.inject.Inject;

import dao.product.IProductBundleSaleUpdateDao;
import dto.product.ProductBundleSale;

public class ProductBundleSaleUpdateDao implements IProductBundleSaleUpdateDao {
	@Inject
	ProductBundleSaleMapper productBundleSaleMapper;
	@Override
	public int deleteAuto() {
		// TODO Auto-generated method stub
		return productBundleSaleMapper.deleteAuto();
	}

	@Override
	public int alterAutoBundleSaleVisible() {
		// TODO Auto-generated method stub
		return productBundleSaleMapper.alterAutoBundleSaleVisible();
	}
	
	@Override
	public int activeBundle(String listing,String bundListing) {
		return productBundleSaleMapper.activeBundle(listing, bundListing);
	}

	@Override
	public int insert(ProductBundleSale record) {
		return productBundleSaleMapper.insert(record);
	}
}
