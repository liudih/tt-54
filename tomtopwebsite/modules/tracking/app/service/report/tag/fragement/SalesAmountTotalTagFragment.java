package service.report.tag.fragement;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import service.affiliate.DateRange;
import service.affiliate.DoubleUtils;
import service.tracking.IAffiliateService;
import services.base.CurrencyService;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.ILoginService;
import services.order.IOrderCurrencyRateService;
import services.order.IOrderEnquiryService;
import dto.order.Order;
import dto.order.OrderCurrencyRate;

public class SalesAmountTotalTagFragment implements ITemplateFragmentProvider {

	final String targetCurrency = "USD";

	@Override
	public String getName() {

		return "sales-amoun-total-tag";
	}

	@Inject
	ILoginService loginService;

	@Inject
	IAffiliateService affiliateService;

	@Inject
	IOrderEnquiryService orderService;

	@Inject
	IOrderCurrencyRateService rateService;

	@Inject
	CurrencyService currencyService;

	@Override
	public Html getFragment(Context context) {
		String email = loginService.getLoginData().getEmail();
		String aid = affiliateService.getAidByEmail(email);
		DateRange range = new DateRange(-30);
		List<Order> orders = orderService.getOrdersByDateRange(aid,
				range.getBegin(), range.getEnd());
		double total = 0.00;
		double rate, sale;
		for (Order order : orders) {
			OrderCurrencyRate orderRate = rateService.getByOrderNumber(order
					.getCordernumber());
			double shippingPrice = 0.0;
			if (null != order.getFshippingprice()) {
				shippingPrice = order.getFshippingprice();
			}
			double salePrice = order.getFgrandtotal()
					- shippingPrice;

			if (orderRate != null) {
				rate = orderRate.getFrate();
				sale = salePrice / rate;
			} else {
				String currency = order.getCcurrency();
				sale = currencyService.exchange(salePrice, currency,
						targetCurrency);
			}
			total += sale;
		}
		return views.html.report.tag.fragement.sales_amount_total_tag
				.render(DoubleUtils.format(total, "0.00"));
	}

}
