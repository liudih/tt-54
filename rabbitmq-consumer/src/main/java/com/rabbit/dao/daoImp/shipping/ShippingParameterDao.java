package com.rabbit.dao.daoImp.shipping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbit.conf.ordermapper.shipping.ShippingParameterMapper;
import com.rabbit.dto.shipping.ShippingParameter;

@Component
public class ShippingParameterDao {
	@Autowired
	ShippingParameterMapper mapper;

	public List<ShippingParameter> getAll() {
		return mapper.getAll();
	}

	public ShippingParameter getByKey(String key) {
		return mapper.getByKey(key);
	}
}
