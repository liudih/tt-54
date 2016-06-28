package plugins.mobile.order;

import play.twirl.api.Html;
import services.ICurrencyService;
import services.base.FoundationService;

import com.google.inject.Inject;

import dto.Currency;
import dto.order.Order;

/**
 * 产品详情
 * 
 * @author lijun
 *
 */
public class OrderProductDetailPlugin implements IOrderDetailPlugin {
	private static final String NAME = "product-detail";

	@Inject
	FoundationService foundation;

	@Inject
	ICurrencyService currencyService;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Html render(Order order) {
		String code = order.getCcurrency();
		Currency currency = currencyService.getCurrencyByCode(code);

		return views.html.mobile.order.orderNumDetail.render(order, currency);
	}
}
