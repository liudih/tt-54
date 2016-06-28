package dao.product.impl;

import mapper.product.ProductBundleSaleMapper;

import com.google.inject.Inject;

import dao.product.IProductBundleSaleEnquiryDao;

public class ProductBundleSaleEnquiryDao implements IProductBundleSaleEnquiryDao {
	@Inject
	ProductBundleSaleMapper productBundleSaleMapper;

	@Override
	public boolean isExist(String listing, String bundleListing) {
		// TODO Auto-generated method stub
		int icount = productBundleSaleMapper.getCountByListingAndBundleListing(listing, bundleListing);
		if(icount==0) {
			return false;
		}
		return true;
	}

}
