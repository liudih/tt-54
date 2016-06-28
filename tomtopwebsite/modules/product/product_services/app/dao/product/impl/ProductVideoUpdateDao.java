package dao.product.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.product.ProductVideoMapper;
import dao.product.IProductVideoUpdateDao;
import dto.product.ProductVideo;

public class ProductVideoUpdateDao implements IProductVideoUpdateDao {

	@Inject
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
