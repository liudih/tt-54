package com.tomtop.product.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tomtop.product.dao.IProductLabelDao;
import com.tomtop.product.models.dto.LabelDto;

@Repository("productLabelDao")
public class ProductLabelDaoImpl implements IProductLabelDao {

	@Override
	public List<LabelDto> getLabels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LabelDto> getLabels(String labelCode) {
		// TODO Auto-generated method stub
		return null;
	}
}