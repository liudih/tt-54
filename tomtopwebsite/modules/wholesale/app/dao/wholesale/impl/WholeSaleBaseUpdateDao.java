package dao.wholesale.impl;

import mapper.wholesale.WholeSaleBaseMapper;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleBaseUpdateDao;
import entity.wholesale.WholeSaleBase;

public class WholeSaleBaseUpdateDao implements IWholeSaleBaseUpdateDao {
	@Inject
	WholeSaleBaseMapper wholeSaleBaseMapper;

	@Override
	public int addWholeSaleBase(WholeSaleBase record) {
		return wholeSaleBaseMapper.addWholeSaleBase(record);
	}

	@Override
	public int updateStatusByIid(Integer iid, Integer istatus) {
		return wholeSaleBaseMapper.updateStatusByIid(iid, istatus);
	}

	@Override
	public int updateWholeSaleBase(WholeSaleBase wholeSaleBase) {
		return wholeSaleBaseMapper.updateWholeSaleBase(wholeSaleBase);
	}

}
