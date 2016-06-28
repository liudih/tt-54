package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductVideoMapper;
import com.rabbit.dao.idao.product.IProductVideoUpdateDao;
import com.rabbit.dto.product.ProductVideo;
@Component
public class ProductVideoUpdateDao implements IProductVideoUpdateDao {

	@Autowired
	ProductVideoMapper productVideoMapper;

	@Override
	public int addProductVideoList(List<ProductVideo> list) {

		return this.productVideoMapper.addProductVideoList(list);
	}
	
	@Override
	public int deleteByListingId(String listingId) {

		return this.productVideoMapper.deleteByListingId(listingId);
	}

}
