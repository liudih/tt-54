package dao.product.impl;

import java.util.List;

import mapper.product.ProductExplainTypeMapper;

import com.google.inject.Inject;

import dao.product.IProductExplainTypeEnquiryDao;
import dto.product.ProductExplainType;

public class ProductExplainTypeEnquiryDao implements
		IProductExplainTypeEnquiryDao {
	@Inject
	ProductExplainTypeMapper productExplainTypeMapper;

	@Override
	public List<ProductExplainType> getAllExplainType() {
		return productExplainTypeMapper.getAllProductExplainType();
	}

}
