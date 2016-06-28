package dao.wholesale.impl;

import mapper.wholesale.WholeSaleCategoryMapper;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleCategoryUpdateDao;
import entity.wholesale.WholeSaleCategory;

public class WholeSaleCategoryUpdateDao implements IWholeSaleCategoryUpdateDao {
	@Inject
	WholeSaleCategoryMapper wholeSaleCategoryMapper;

	@Override
	public int deleteByWholeSaleId(Integer wholeSaleId) {
		return wholeSaleCategoryMapper.deleteByWholeSaleId(wholeSaleId);
	}

	@Override
	public int addWholeSaleCategory(WholeSaleCategory record) {
		return wholeSaleCategoryMapper.addWholeSaleCategory(record);
	}

}
