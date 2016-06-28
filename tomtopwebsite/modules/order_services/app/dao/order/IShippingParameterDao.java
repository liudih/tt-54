package dao.order;

import java.util.List;

import dto.shipping.ShippingParameter;

public interface IShippingParameterDao {
	List<ShippingParameter> getAll();

	ShippingParameter getByKey(String key);
}
