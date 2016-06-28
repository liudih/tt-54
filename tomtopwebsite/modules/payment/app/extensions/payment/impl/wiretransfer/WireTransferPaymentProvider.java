package extensions.payment.impl.wiretransfer;

import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import mapper.order.PaymentAccountMapper;
import play.data.DynamicForm;
import play.twirl.api.Html;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.JsonFormatUtils;
import services.base.utils.StringUtils;
import services.order.OrderUpdateService;
import services.payment.OceanPaymentService;
import valueobjects.order_api.payment.PaymentContext;
import valueobjects.payment.SummaryInformation;

import com.google.common.collect.Maps;

import dto.order.Order;
import dto.order.OrderDetail;
import enums.OrderLableEnum;
import extensions.payment.IPaymentProvider;

public class WireTransferPaymentProvider implements IPaymentProvider {

	@Inject
	PaymentAccountMapper accountMapper;

	@Inject
	OceanPaymentService summaryService;

	@Inject
	CurrencyService currencyService;

	@Inject
	FoundationService foundation;

	@Inject
	OrderUpdateService updateService;

	@Override
	public String id() {
		return "wiretransfer";
	}

	@Override
	public String name() {
		return "Bank to Bank Wire Transfer";
	}

	@Override
	public int getDisplayOrder() {
		return 700;
	}

	@Override
	public String iconUrl() {
		return controllers.payment.impl.routes.Assets.at(
				"/images/payment06.png").url();
	}

	@Override
	public Html getDescription(PaymentContext context) {
		if (context == null || context.isModeNew()) {
			return views.html.payment.wiretransfer.v2.description.render();
		} else {
			return views.html.payment.wiretransfer.description.render();
		}

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
		if (StringUtils.isEmpty(accountString)) {
			return null;
		}
		LinkedHashMap<String, String> account = JsonFormatUtils.jsonToBean(
				accountString, LinkedHashMap.class);
		List<SummaryInformation> list = summaryService.getInformations(order
				.getIid());
		updatePaymentAccount(context, account.get("account"));
		return views.html.payment.paymentOrderDetail.render(context, this,
				account, list);
	}

	private void updatePaymentAccount(PaymentContext context, String account) {
		if (OrderLableEnum.DROP_SHIPPING.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, account);
		} else if (OrderLableEnum.TOTAL_ORDER.getName().equals(
				context.getOrderLable())) {
			updateAccountTotal(context, account);
		} else {
			updateAccountDefault(context, account);
		}
	}

	private void updateAccountDefault(PaymentContext context, String account) {
		updateService.updatePaymentAccount(context.getOrder().getOrder()
				.getIid(), account);
	}

	private void updateAccountTotal(PaymentContext context, String account) {
		List<OrderDetail> details = context.getOrder().getDetails();
		for (OrderDetail detail : details) {
			updateService.updatePaymentAccount(detail.getIorderid(), account);
		}
	}

	@Override
	public Html getDoPaymentHtml(PaymentContext context) {
		String url = controllers.payment.routes.Payment.toWireTransfer().url();
		LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
		map.put("orderId", context.getOrder().getOrder().getCordernumber());
		return views.html.payment.form.render(url, map);
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
