package services.order.fragment.provider;

import java.util.List;

import javax.inject.Inject;

import services.order.IOrderFragmentProvider;
import services.order.IOrderService;
import valueobjects.cart.CartItem;
import valueobjects.order_api.Carts;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.OrderItem;
import valueobjects.order_api.OrderItems;

public class OrderCartProductProvider implements IOrderFragmentProvider {
	@Inject
	private IOrderService orderService;

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		//modify by lijun
		List<CartItem> items = context.getItems();
		if(items != null){
			return new Carts(items);
		}else{
			return new Carts(context.getCart());
		}
		
	}

	@Override
	public IOrderFragment getExistingFragment(ExistingOrderContext context) {
		List<OrderItem> list = orderService.getOrderDetailByOrder(context
				.getOrder());
		return new OrderItems(list);
	}
}
