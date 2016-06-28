package services.order.fragment.provider;

import java.util.List;

import services.order.IOrderFragmentProvider;
import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;
import valueobjects.order_api.OrderSummaryInfo;

import com.google.common.collect.Lists;

import dto.order.Order;
import facades.cart.Cart;

public class OrderSummaryProvider implements IOrderFragmentProvider {

	@Override
	public IOrderFragment getFragment(OrderContext context) {
		Cart cart = context.getCart();
		double baseTotal = cart.getTotal();
		List<ExtraLineView> extrasInfos = cart.convertExtras();
		double discount = 0.0;
		for (ExtraLineView extraLineView : extrasInfos) {
			discount += extraLineView.getMoney();
		}
		return new OrderSummaryInfo(baseTotal, extrasInfos, discount);
	}

	@Override
	public IOrderFragment getExistingFragment(ExistingOrderContext context) {
		Order order = context.getOrder();
		return new OrderSummaryInfo(order.getFordersubtotal(),
				Lists.newArrayList(), 0.0);
	}

}
