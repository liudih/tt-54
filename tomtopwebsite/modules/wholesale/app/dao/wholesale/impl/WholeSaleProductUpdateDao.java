package dao.wholesale.impl;

import java.util.List;

import mapper.wholesale.WholeSaleProductMapper;

import com.google.inject.Inject;

import dao.wholesale.IWholeSaleProductUpdateDao;
import entity.wholesale.WholeSaleProduct;

public class WholeSaleProductUpdateDao implements IWholeSaleProductUpdateDao {
	@Inject
	WholeSaleProductMapper mapper;

	@Override
	public int deleteByIid(Integer iid, String email) {
		return mapper.deleteByIid(iid, email);
	}

	@Override
	public int addWholeSaleProduct(WholeSaleProduct record) {
		return mapper.addWholeSaleProduct(record);
	}

	@Override
	public int updateQtyByIid(Integer iid, Integer qty) {
		return mapper.updateQtyByIid(iid, qty);
	}

	@Override
	public int batchDeleteByIid(List<Integer> ids, String email) {
		return mapper.batchDeleteByIids(ids, email);
	}

}
