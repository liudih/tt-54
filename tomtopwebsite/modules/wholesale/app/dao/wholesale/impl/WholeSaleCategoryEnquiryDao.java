package dao.wholesale.impl;

import java.util.List;

import mapper.wholesale.WholeSaleCategoryMapper;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleCategoryEnquiryDao;
import entity.wholesale.WholeSaleCategory;

public class WholeSaleCategoryEnquiryDao implements
		IWholeSaleCategoryEnquiryDao {
	@Inject
	WholeSaleCategoryMapper wholeSaleCategoryMapper;

	@Override
	public List<WholeSaleCategory> getWholeSaleCategoryByWholeSaleId(
			Integer wholeSaleId) {
		return wholeSaleCategoryMapper
				.getWholeSaleCategoryByWholeSaleId(wholeSaleId);
	}

}
