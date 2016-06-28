package services.mobile.shipping;

import java.util.List;

import services.mobile.MobileService;
import services.shipping.IShippingMethodService;

import com.google.inject.Inject;

import dto.ShippingMethodDetail;

public class ShippingService {

	@Inject
	IShippingMethodService shippingMethodService;

	@Inject
	MobileService mobileService;

	public List<ShippingMethodDetail> getShippingMethods() {

		return null;
	}

}
