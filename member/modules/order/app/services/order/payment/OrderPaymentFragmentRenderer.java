package services.order.payment;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import play.twirl.api.Html;
import services.base.utils.StringUtils;
import services.order.ExistingOrderRenderContext;
import services.order.IOrderFragmentRenderer;
import services.order.OrderRenderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.payment.PaymentContext;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import extensions.payment.IPaymentProvider;

public class OrderPaymentFragmentRenderer implements IOrderFragmentRenderer {

	@Inject
	Set<IPaymentProvider> paymentProviders;

	@Override
	public Html render(IOrderFragment fragment, OrderRenderContext context) {
		return renderWithPaymentContext(null, null);
	}

	public Html renderWithPaymentContext(String payment, PaymentContext context) {
		List<IPaymentProvider> sorted = Ordering.natural()
				.onResultOf((IPaymentProvider p) -> p.getDisplayOrder())
				.sortedCopy(paymentProviders);
		return views.html.order.payment.v2.select.render(sorted, payment, context);
	}

	public Html filterRender(String payment, List<String> payments,
			PaymentContext context) {
		List<IPaymentProvider> sorted = Ordering.natural()
				.onResultOf((IPaymentProvider p) -> p.getDisplayOrder())
				.sortedCopy(paymentProviders);
		if (payments != null && !payments.isEmpty()) {
			sorted = Lists.newArrayList(Collections2.filter(sorted,
					p -> payments.contains(p.id())));
		}
		return views.html.order.payment.select.render(sorted, payment, context);
	}

	public Html newFilterRender(String payment, List<String> payments,
			PaymentContext context) {
		List<IPaymentProvider> sorted = Ordering.natural()
				.onResultOf((IPaymentProvider p) -> p.getDisplayOrder())
				.sortedCopy(paymentProviders);
		String countryShortName = context.getOrder().getOrder().getCcountrysn();
		if (payments != null && !payments.isEmpty()) {
			sorted = Lists.newArrayList(Collections2.filter(sorted,
					p -> payments.contains(p.id())));
		}
		if (StringUtils.notEmpty(countryShortName)) {
			sorted = Lists.newArrayList(Collections2.filter(
					sorted,
					p -> countryShortName.equals(p.area())
							|| "GLOBAL".equals(p.area())));
		}
		return views.html.order.payment.new_select.render(sorted, payment,
				context);
	}

	@Override
	public Html renderExisting(IOrderFragment fragment,
			ExistingOrderRenderContext context) {
		return renderWithPaymentContext(null, null);
	}

}
