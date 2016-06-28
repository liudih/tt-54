package service.report.tag.fragement;

import java.util.List;

import javax.inject.Inject;

import play.mvc.Http.Context;
import play.twirl.api.Html;
import service.affiliate.DateRange;
import service.affiliate.DoubleUtils;
import service.tracking.CommissionService;
import service.tracking.IAffiliateService;
import services.ICurrencyService;
import services.base.template.ITemplateFragmentProvider;
import services.member.login.ILoginService;
import services.order.IOrderCurrencyRateService;
import services.order.IOrderEnquiryService;
import dto.order.Order;
import dto.order.OrderCurrencyRate;

public class CommissionTotalTagFragment implements ITemplateFragmentProvider {

	final String targetCurrency = "USD";

	@Override
	public String getName() {

		return "commission-total-tag";
	}

	@Inject
	ILoginService loginService;

	@Inject
	IAffiliateService affiliateService;

	@Inject
	IOrderEnquiryService orderService;

	@Inject
	CommissionService commissionService;

	@Inject
	IOrderCurrencyRateService rateService;

	@Inject
	ICurrencyService currencyService;

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
			double fgtotal = order.getFgrandtotal() == null ? 0 : order
					.getFgrandtotal();
			double fsprice = order.getFshippingprice() == null ? 0 : order
					.getFshippingprice();
			double salePrice = fgtotal - fsprice;
			if (salePrice < 0) {
				salePrice = 0;
			}

			OrderCurrencyRate orderRate = rateService.getByOrderNumber(order
					.getCordernumber());
			if (orderRate != null) {
				rate = orderRate.getFrate();
				sale = salePrice / rate;
			} else {
				String currency = order.getCcurrency();
				sale = currencyService.exchange(salePrice, currency,
						targetCurrency);
			}
			double cRate = commissionService.getCommissionRate(aid);
			double commisssion = sale * cRate;
			total += commisssion;
		}
		return views.html.report.tag.fragement.commission_total_tag
				.render(DoubleUtils.format(total, "0.00"));
	}

}
