package com.rabbit.dao.daoImp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.mapper.product.ProductUrlMapper;
import com.rabbit.dao.idao.product.IProductUrlUpdateDao;
import com.rabbit.dto.product.ProductUrl;
@Component
public class ProductUrlUpdateDao implements IProductUrlUpdateDao {

	@Autowired
	ProductUrlMapper productUrlMapper;

	@Override
	public int addProductUrl(ProductUrl record) {

		return this.productUrlMapper.addProductUrl(record);
	}

	@Override
	public int addProductUrlList(List<ProductUrl> list) {

		return this.productUrlMapper.addProductUrlList(list);
	}

	@Override
	public int deleteByListingId(String listingId) {

		return this.productUrlMapper.deleteByListingId(listingId);
	}

}
