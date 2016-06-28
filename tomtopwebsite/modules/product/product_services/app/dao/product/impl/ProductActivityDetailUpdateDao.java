package dao.product.impl;

import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductActivityRelationDetailMapper;
import dao.product.IProductActivityDetailUpdateDao;

public class ProductActivityDetailUpdateDao implements
		IProductActivityDetailUpdateDao {

	@Inject
	ProductActivityRelationDetailMapper activityRelationMapper;

	@Override
	public int addProductRelationDetail(Map<String, Object> param) {
		return activityRelationMapper.addProductRelationDetail(param);
	}
	
	@Override
	public int updateProductByIidAndListingid(double price,int qty, int aid, String listingid){
		return activityRelationMapper.updateProductByIidAndListingid(price,qty,aid,listingid);
	}
	
	@Override
	public int updatePriceyIidAndListingid(double price, int aid, String listingid){
		return activityRelationMapper.updatePriceByIidAndListingid(price, aid, listingid);
	}

}
