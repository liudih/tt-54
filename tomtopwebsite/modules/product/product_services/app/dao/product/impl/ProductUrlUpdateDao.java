package dao.product.impl;

import java.util.List;

import mapper.product.ProductUrlMapper;

import com.google.inject.Inject;

import dao.product.IProductUrlUpdateDao;
import dto.product.ProductUrl;

public class ProductUrlUpdateDao implements IProductUrlUpdateDao {

	@Inject
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
