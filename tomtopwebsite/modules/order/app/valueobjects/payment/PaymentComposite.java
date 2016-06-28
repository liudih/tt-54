package valueobjects.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import valueobjects.order_api.payment.PaymentContext;
import extensions.payment.IPaymentProvider;

public class PaymentComposite {
	final Map<String, IPaymentProvider> paymentMap = new HashMap<String, IPaymentProvider>();
	final PaymentContext paymentContext;

	public PaymentComposite(PaymentContext context) {
		this.paymentContext = context;
	}

	public void put(String name, IPaymentProvider provider) {
		paymentMap.put(name, provider);
	}

	public IPaymentProvider get(String name) {
		return paymentMap.get(name);
	}

	public Set<String> keySet() {
		return paymentMap.keySet();
	}

	public Map<String, IPaymentProvider> getPaymentsMap() {
		return paymentMap;
	}

	public PaymentContext getPaymentContext() {
		return paymentContext;
	}

}
