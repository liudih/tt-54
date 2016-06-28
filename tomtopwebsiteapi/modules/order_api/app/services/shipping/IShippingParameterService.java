package services.shipping;

import java.util.Map;

import dto.shipping.ShippingParameter;

public interface IShippingParameterService {

	public abstract Map<String, String> getAll();

	public abstract ShippingParameter getByKey(String key);

}