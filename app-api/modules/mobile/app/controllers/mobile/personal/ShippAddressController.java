package controllers.mobile.personal;

import java.util.List;

import javax.inject.Inject;

import dto.Country;
import dto.Currency;
import dto.order.Order;
import dto.order.OrderDetail;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;
import plugins.provider.IOrderFragmentProvider;
import plugins.provider.OrderShippingMethodProvider;
import services.ICountryService;
import services.ICurrencyService;
import services.order.IOrderEnquiryService;
import valueobjects.order_api.ShippingMethodInformations;

public class ShippAddressController extends Controller {
	
	@Inject
	private OrderShippingMethodProvider prvider;
	@Inject
	private ICountryService countryService;
	@Inject
	private IOrderEnquiryService orderEnquiry;
	@Inject
	private ICurrencyService currencyService;
	
	
	public Result refreshShipMethodForGuest(String orderNumber,
			String shipToCountryCode) {
		Logger.debug("orderNumber == " + orderNumber);
		Logger.debug("shipToCountryCode == " + shipToCountryCode);
		if (orderNumber == null || orderNumber.length() == 0
				|| shipToCountryCode == null || shipToCountryCode.length() == 0) {
			return badRequest();
		}
		// 某些国家被屏蔽
		Country country = countryService
				.getCountryByShortCountryName(shipToCountryCode);
		Boolean isShow = country.getBshow();
		if (isShow != null && !isShow) {
			Logger.debug("country:{} is not show,so can not get ship method",
					shipToCountryCode);
			return ok(Html.apply(""));
		}

		try {
			Order order = orderEnquiry.getOrderById(orderNumber);
			if (null != order) {
				List<OrderDetail> details = orderEnquiry
						.getOrderDetails(orderNumber);
				Currency currency = currencyService.getCurrencyByCode(order
						.getCcurrency());

				Integer storageID = order.getIstorageid();
				if (storageID == null) {
					Logger.debug("order:{} storageID is null",
							order.getCordernumber());
					return badRequest();
				}
				ShippingMethodInformations fragment = (ShippingMethodInformations) prvider.
						getExistingFragment(order, storageID, country, details);
				return ok(views.html.mobile.pay.paypal_shipping_method.render(
						fragment, currency));
			}
		} catch (Exception e) {
			Logger.error("ShippingAddress refreshShippingMethod", e);
		}
		return badRequest();
	}
}
