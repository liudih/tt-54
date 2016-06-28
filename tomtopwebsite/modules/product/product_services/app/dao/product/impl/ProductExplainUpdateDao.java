package dao.product.impl;

import mapper.product.ProductExplainMapper;

import com.google.inject.Inject;

import dao.product.IProductExplainUpdateDao;
import dto.product.ProductExplain;

public class ProductExplainUpdateDao implements IProductExplainUpdateDao {
	@Inject
	ProductExplainMapper productExplainMapper;

	@Override
	public boolean updateProductExplain(ProductExplain productExplain) {
		return productExplainMapper.updateProductExplain(productExplain) > 0;
	}

	@Override
	public boolean addProductExplain(ProductExplain productExplain) {
		return productExplainMapper.addProductExplain(productExplain) > 0;
	}

	@Override
	public boolean deleteByIid(Integer iid) {
		return productExplainMapper.deleteByIid(iid) > 0;
	}
}
