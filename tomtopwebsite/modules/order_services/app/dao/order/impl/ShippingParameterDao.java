package dao.order.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.order.ShippingParameterMapper;
import dao.order.IShippingParameterDao;
import dto.shipping.ShippingParameter;

public class ShippingParameterDao implements IShippingParameterDao {
	@Inject
	ShippingParameterMapper mapper;

	@Override
	public List<ShippingParameter> getAll() {
		return mapper.getAll();
	}

	@Override
	public ShippingParameter getByKey(String key) {
		return mapper.getByKey(key);
	}

}
