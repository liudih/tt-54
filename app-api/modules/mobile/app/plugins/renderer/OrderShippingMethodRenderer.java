package plugins.renderer;


import play.twirl.api.Html;
import services.ICurrencyService;
import services.mobile.MobileService;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.ShippingMethodInformations;

import com.google.inject.Inject;

import dto.Currency;

public class OrderShippingMethodRenderer implements IOrderFragmentRenderer {

	@Inject
	MobileService mobileService;

	@Inject 
	ICurrencyService currencyService;
	
	@Override
	public Html render(IOrderFragment fragment) {
		if (fragment == null) {
			return Html.apply("");
		}
		ShippingMethodInformations methods = (ShippingMethodInformations) fragment;
		String curid = mobileService.getCurrency();
		Currency currency = currencyService.getCurrencyByCode(curid);
		
		return views.html.mobile.pay.paypal_shipping_method.render(
				methods, currency);
	}
}
