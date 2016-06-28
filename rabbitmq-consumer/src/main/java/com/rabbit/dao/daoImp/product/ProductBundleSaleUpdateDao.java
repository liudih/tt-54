package com.rabbit.dao.daoImp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductBundleSaleMapper;
import com.rabbit.dao.idao.product.IProductBundleSaleUpdateDao;
import com.rabbit.dto.product.ProductBundleSale;
@Component
public class ProductBundleSaleUpdateDao implements IProductBundleSaleUpdateDao {
	@Autowired
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
