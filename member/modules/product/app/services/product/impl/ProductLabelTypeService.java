package services.product.impl;

import java.util.List;

import services.product.IProductLabelTypeService;

import com.google.inject.Inject;

import dao.product.IProductLabelTypeDao;
import dto.product.ProductLabelType;

/**
 * 
 * @author lijun
 *
 */
public class ProductLabelTypeService implements IProductLabelTypeService {
	@Inject
	IProductLabelTypeDao dao;

	@Override
	public int deleteById(Integer id) {
		return this.dao.deleteById(id);
	}

	@Override
	public int deleteByCreater(String creater) {
		return this.dao.deleteByCreater(creater);
	}

	@Override
	public int insert(ProductLabelType type) {
		return this.dao.insert(type);
	}

	@Override
	public int batchInsert(List<ProductLabelType> types) {
		return this.dao.batchInsert(types);
	}

	@Override
	public ProductLabelType selectById(int id) {
		return this.dao.selectById(id);
	}

	@Override
	public List<ProductLabelType> selectAll() {
		return this.dao.selectAll();
	}

}
