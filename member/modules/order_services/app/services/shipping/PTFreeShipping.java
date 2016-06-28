package services.shipping;

import java.util.List;

import javax.inject.Inject;

import services.base.CurrencyService;
import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.shipping.ShippingMethodRequst;
import extensions.order.shipping.IFreightPlugin;

/**
 * 如果存在免费，且订单中商品全为免邮，总价超过 30 USD，则免邮的物流费用为0
 * 
 * @author luoJH
 *
 */
public class PTFreeShipping implements IFreightPlugin {
	@Inject
	CurrencyService currencyService;

	@Override
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst) {
		double usdTotal = currencyService.exchange(requst.getGrandTotal(),
				requst.getCurrency(), "USD");
		for (ShippingMethodInformation shippingMethodInformation : list) {
			if (shippingMethodInformation.isFree()) {
				if (usdTotal >= 30 && requst.getShippingWeight() == 0) {
					shippingMethodInformation.setFreight(0.0);
				}
			}
		}
		return list;
	}

	@Override
	public int order() {
		return 100;
	}

}
