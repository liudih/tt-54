package extensions.payment.impl.westunion;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import mapper.order.PaymentAccountMapper;
import play.data.DynamicForm;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.JsonFormatUtils;
import services.payment.OceanPaymentService;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.payment.SummaryInformation;
import dto.order.Order;
import extensions.payment.IPaymentProvider;

public class WesternUnionPaymentProvider implements IPaymentProvider {

	@Inject
	PaymentAccountMapper accountMapper;

	@Inject
	OceanPaymentService summaryService;

	@Inject
	CurrencyService currencyService;

	@Inject
	FoundationService foundation;

	@Override
	public String id() {
		return "westunion";
	}

	@Override
	public String name() {
		return "Western Union";
	}

	@Override
	public int getDisplayOrder() {
		return 600;
	}

	@Override
	public String iconUrl() {
		return controllers.payment.impl.routes.Assets.at(
				"/images/payment/westernunion/icon.jpg").url();
	}

	@Override
	public Html getDescription(PaymentContext context) {
		return views.html.payment.westunion.description.render();
	}

	@Override
	public boolean isManualProcess() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Html getInstruction(PaymentContext context) {
		Order order = context.getOrder().getOrder();
		String ccy = foundation.getCurrency();
		String accountString = accountMapper.getAccount(order.getIwebsiteid(),
				currencyService.exchange(order.getFgrandtotal(), ccy, "USD"),
				order.getCpaymentid());
		LinkedHashMap<String, String> account = JsonFormatUtils.jsonToBean(
				accountString, LinkedHashMap.class);
		List<SummaryInformation> list = summaryService.getInformations(order
				.getIid());
		return views.html.payment.paymentOrderDetail.render(context, this,
				account, list);
	}

	@Override
	public Html getDoPaymentHtml(PaymentContext context) {
		return null;
	}

	@Override
	public boolean isNeedExtraInfo() {
		return false;
	}

	@Override
	public boolean validForm(DynamicForm df) {
		return false;
	}

	@Override
	public String area() {
		return "GLOBAL";
	}

}
