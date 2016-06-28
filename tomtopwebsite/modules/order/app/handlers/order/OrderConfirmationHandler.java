package handlers.order;

import javax.inject.Inject;

import mapper.cart.CartBaseMapper;
import services.base.CurrencyService;
import services.order.IOrderEnquiryService;
import services.order.OrderCurrencyRateService;

import com.google.common.eventbus.Subscribe;

import dto.order.Order;
import dto.order.OrderCurrencyRate;
import events.order.OrderConfirmationEvent;

public class OrderConfirmationHandler {
	@Inject
	private CartBaseMapper cartBaseMapper;
	@Inject
	private IOrderEnquiryService orderEnquiryService;
	@Inject
	private CurrencyService currencyService;
	@Inject
	private OrderCurrencyRateService rateService;

	@Subscribe
	public void onOrderConfirmation(OrderConfirmationEvent event) {
		if (event.getIcartid() != null) {
			cartBaseMapper.updateCartStatusByCartId(event.getIcartid());
		}
		Order order = orderEnquiryService.getOrderById(event.getIorderid());
		saveOrderCurrencyRate(event, order);
	}

	private void saveOrderCurrencyRate(OrderConfirmationEvent event, Order order) {
		if (null != order) {
			double frate = currencyService.getRate(order.getCcurrency());
			OrderCurrencyRate rate = new OrderCurrencyRate();
			rate.setCcurrency(order.getCcurrency());
			rate.setCordernumber(order.getCordernumber());
			rate.setFrate(frate);
			rateService.insert(rate);
		}
	}

}
