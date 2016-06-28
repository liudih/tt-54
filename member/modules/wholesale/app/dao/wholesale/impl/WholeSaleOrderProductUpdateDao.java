package dao.wholesale.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.wholesale.WholeSaleOrderProductMapper;
import dao.wholesale.IWholeSaleOrderProductUpdateDao;
import entity.wholesale.WholeSaleOrderProduct;

public class WholeSaleOrderProductUpdateDao implements
		IWholeSaleOrderProductUpdateDao {
	@Inject
	WholeSaleOrderProductMapper mapper;

	@Override
	public int insert(WholeSaleOrderProduct product) {
		return mapper.insert(product);
	}

	@Override
	public int batchInsert(List<WholeSaleOrderProduct> list) {
		return mapper.batchInsert(list);
	}

}
