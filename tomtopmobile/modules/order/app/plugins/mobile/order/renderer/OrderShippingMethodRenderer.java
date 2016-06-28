package plugins.mobile.order.renderer;

import play.twirl.api.Html;
import services.base.FoundationService;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.ShippingMethodInformations;

import com.google.inject.Inject;

import dto.Currency;

public class OrderShippingMethodRenderer implements IOrderFragmentRenderer {

	@Inject
	FoundationService foundationService;

	@Override
	public Html render(IOrderFragment fragment) {
		if (fragment == null) {
			return Html.apply("");
		}
		ShippingMethodInformations methods = (ShippingMethodInformations) fragment;
		Currency currency = foundationService.getCurrencyObj();
		return views.html.mobile.order.shipping_method_information.render(
				methods, currency);
	}
}
