package dao.wholesale.impl;

import javax.inject.Inject;

import mapper.wholesale.WholeSaleDiscountLevelMapper;
import dao.wholesale.IWholeSaleDiscountLevelUpdateDao;
import entity.wholesale.WholeSaleDiscountLevel;

public class WholeSaleDiscountLevelUpdateDao implements
		IWholeSaleDiscountLevelUpdateDao {
	@Inject
	WholeSaleDiscountLevelMapper mapper;

	@Override
	public int update(WholeSaleDiscountLevel discount) {
		return mapper.update(discount);
	}

	@Override
	public int insert(WholeSaleDiscountLevel discount) {
		return mapper.insert(discount);
	}

	@Override
	public int delete(int id) {
		return mapper.delete(id);
	}

}
