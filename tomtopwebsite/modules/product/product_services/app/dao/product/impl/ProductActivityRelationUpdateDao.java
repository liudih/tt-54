package dao.product.impl;

import java.util.Map;

import javax.inject.Inject;

import mapper.product.ProductActivityRelationMapper;
import dao.product.IProductActivityRelationUpdateDao;

public class ProductActivityRelationUpdateDao implements
		IProductActivityRelationUpdateDao {

	@Inject
	ProductActivityRelationMapper mapper;

	@Override
	public int addProductRelation(Map<String, Object> param) {
		return this.mapper.addProductRelation(param);
	}

}
