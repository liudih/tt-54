package com.rabbit.dao.daoImp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductBundleSaleMapper;
import com.rabbit.dao.idao.product.IProductBundleSaleEnquiryDao;
@Component
public class ProductBundleSaleEnquiryDao implements IProductBundleSaleEnquiryDao {
	@Autowired
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
