package dao.product.impl;

import java.util.List;

import mapper.product.ProductTranslateMapper;

import com.google.inject.Inject;

import dao.product.IProductTranslateUpdateDao;
import dto.product.ProductTranslate;

public class ProductTranslateUpdateDao implements IProductTranslateUpdateDao {

	@Inject
	ProductTranslateMapper productTranslateMapper;
	
	@Override
	public int deleteByIid(Integer iid) {
		return this.productTranslateMapper.deleteByIid(iid);
	}

	@Override
	public int addProductTranslate(ProductTranslate record) {
		return this.productTranslateMapper.addProductTranslate(record);
	}

	@Override
	public int addSelectiveProductTranslate(ProductTranslate record) {
		return this.productTranslateMapper.addSelectiveProductTranslate(record);
	}

	@Override
	public int alterSelectiveProductTranslate(ProductTranslate record) {
		return this.productTranslateMapper.alterSelectiveProductTranslate(record);
	}

	@Override
	public int alterProductTranslate(ProductTranslate record) {
		return this.productTranslateMapper.alterProductTranslate(record);
	}

	@Override
	public int addProductTranslateList(List<ProductTranslate> list) {
		return this.productTranslateMapper.addProductTranslateList(list);
	}

	@Override
	public int deleteByListingId(String listingId) {
		return this.productTranslateMapper.deleteByListingId(listingId);
	}

	@Override
	public int deleteByIdvalidListingId(Integer id, String listingId) {
		return this.productTranslateMapper.deleteByIdvalidListingId(id, listingId);
	}

	@Override
	public int alterByListingIdAndLuanguage(ProductTranslate recorde) {
		return this.productTranslateMapper.alterByListingIdAndLuanguage(recorde);
	}
 

}
