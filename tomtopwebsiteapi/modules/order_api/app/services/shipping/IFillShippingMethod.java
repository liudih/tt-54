package services.shipping;

import java.util.List;

import dto.shipping.ShippingMethod;

public interface IFillShippingMethod {

	public abstract List<ShippingMethod> fill(List<ShippingMethod> methods);

}