package services.order.fragment.renderer;

import dto.Currency;
import play.twirl.api.Html;
import services.order.ExistingOrderRenderContext;
import services.order.IOrderFragmentRenderer;
import services.order.OrderRenderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.ShippingMethodInformations;

public class OrderShippingMethodRenderer implements IOrderFragmentRenderer {

	@Override
	public Html render(IOrderFragment fragment, OrderRenderContext context) {
		return views.html.order.shipping_method_information.render(
				(ShippingMethodInformations) fragment, context.getComposite()
						.getCurrency());
	}

	@Override
	public Html renderExisting(IOrderFragment fragment,
			ExistingOrderRenderContext context) {
		return views.html.order.shipping_method_information.render(
				(ShippingMethodInformations) fragment, context.getComposite()
						.getCurrency());
	}

	/**
	 * @author lijun
	 */
	public Html render(IOrderFragment fragment, Currency currency) {

		return views.html.order.shipping_method_information.render(
				(ShippingMethodInformations) fragment, currency);
	}

}
