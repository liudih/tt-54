package com.rabbit.services.serviceImp.shipping;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.rabbit.dao.daoImp.shipping.ShippingParameterDao;
import com.rabbit.dto.shipping.ShippingParameter;
@Service
public class ShippingParameterService {

	@Autowired
	ShippingParameterDao shippingParameterDao;

	/* (non-Javadoc)
	 * @see services.shipping.IShippingParameterService#getAll()
	 */
	public Map<String, String> getAll() {
		List<ShippingParameter> list = shippingParameterDao.getAll();
		return Maps.transformValues(Maps.uniqueIndex(list, e -> e.getCkey()),
				s -> s.getCjsonvalue());
	}

	/* (non-Javadoc)
	 * @see services.shipping.IShippingParameterService#getByKey(java.lang.String)
	 */
	public ShippingParameter getByKey(String key) {
		return shippingParameterDao.getByKey(key);
	}
}
