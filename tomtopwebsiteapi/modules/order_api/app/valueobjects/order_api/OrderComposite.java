package valueobjects.order_api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dto.Currency;

public class OrderComposite implements Serializable {

	final Map<String, IOrderFragment> fragmentsMap = new HashMap<String, IOrderFragment>();

	final OrderContext orderContext;

	final Currency currency;

	public OrderComposite(OrderContext orderContext, Currency currency) {
		this.orderContext = orderContext;
		this.currency = currency;
	}

	public OrderContext getOrderContext() {
		return orderContext;
	}

	public void put(String name, IOrderFragment fragment) {
		fragmentsMap.put(name, fragment);
	}

	public IOrderFragment get(String name) {
		return fragmentsMap.get(name);
	}

	public Currency getCurrency() {
		return currency;
	}

	public Set<String> keySet() {
		return fragmentsMap.keySet();
	}

	public Map<String, IOrderFragment> getFragmentsMap() {
		return fragmentsMap;
	}

}
