package plugins.mobile.order.provider;

import javax.inject.Inject;

import services.order.IOrderService;
import valueobjects.order_api.Carts;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;

public class OrderCartProductProvider implements IOrderFragmentProvider {
	@Inject
	private IOrderService orderService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		return null;
	}

}
