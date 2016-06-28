package services.shipping;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Maps;

import dao.order.IShippingParameterDao;
import dto.shipping.ShippingParameter;

public class ShippingParameterService implements IShippingParameterService {
	@Inject
	IShippingParameterDao shippingParameterDao;

	/* (non-Javadoc)
	 * @see services.shipping.IShippingParameterService#getAll()
	 */
	@Override
	public Map<String, String> getAll() {
		List<ShippingParameter> list = shippingParameterDao.getAll();
		return Maps.transformValues(Maps.uniqueIndex(list, e -> e.getCkey()),
				s -> s.getCjsonvalue());
	}

	/* (non-Javadoc)
	 * @see services.shipping.IShippingParameterService#getByKey(java.lang.String)
	 */
	@Override
	public ShippingParameter getByKey(String key) {
		return shippingParameterDao.getByKey(key);
	}
}
