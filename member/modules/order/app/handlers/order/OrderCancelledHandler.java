package handlers.order;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import services.base.utils.StringUtils;
import services.cart.IExtraLineService;
import valueobjects.order_api.cart.ExtraLine;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import events.order.OrderCancelledEvent;
import extensions.order.IOrderExtrasProvider;

public class OrderCancelledHandler {
	@Inject
	private IExtraLineService extraLineService;
	@Inject
	private Set<IOrderExtrasProvider> extrasProviders;

	@Subscribe
	public void onOrderCancelled(OrderCancelledEvent event) {
		String cartID = event.getOrderValue().getOrder().getCcartid();
		if (StringUtils.isEmpty(cartID)) {
			return;
		}
		Map<String, ExtraLine> extras = extraLineService
				.getExtraLinesByCartId(cartID);
		Map<String, IOrderExtrasProvider> providers = Maps.uniqueIndex(
				extrasProviders, p -> p.getId());
		extras.forEach((id, line) -> {
			IOrderExtrasProvider provider = providers.get(id);
			if (provider != null) {
				provider.cancelledOperation(line);
			}
		});
	}
}
