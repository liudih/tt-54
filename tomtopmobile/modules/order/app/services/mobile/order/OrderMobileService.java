package services.mobile.order;

import java.util.List;

import org.springframework.util.Assert;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

public class OrderMobileService {
	@Inject
	ShippingApiServices shippingApiServices;
	
	/**
	 * 新版校验 物流
	 * @param storageId
	 * @param country
	 * @param shipCode
	 * @param subTotal
	 * @param items
	 * @param cunrrency
	 * @param langID
	 * @return
	 */
	public valueobjects.order.ShippingMethod checkShippingMethodCorrect(int storageId,
			String country, String shipCode, Double subTotal,
			List<valueobjects.cart.CartItem> items, String cunrrency, int langID) {
		Assert.hasLength(shipCode, "ship code is null");
		Assert.hasLength(country, "country code is null");
		Assert.hasLength(cunrrency, "cunrrency code is null");
		Assert.notEmpty(items, "items is null");
		List<valueobjects.order.ShippingMethod> shipMethods = shippingApiServices.getShipMethod(
				country, storageId, langID, items, cunrrency, subTotal);

		if (shipMethods != null) {
			ImmutableList<valueobjects.order.ShippingMethod> hits = FluentIterable
					.from(shipMethods)
					.filter(m -> {
						if(m!=null && m.getCode()!=null && m.getCode().equals(shipCode)){
							return true;
						}else{
							return false;
						}
					}).toList();
			if (hits.size() > 0) {
				return hits.get(0);
			}
		}
		return null;
	}
	
	
}
