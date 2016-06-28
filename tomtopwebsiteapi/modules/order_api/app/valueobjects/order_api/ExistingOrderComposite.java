package valueobjects.order_api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import dto.Currency;

public class ExistingOrderComposite implements Serializable {

	final Map<String, IOrderFragment> fragmentsMap = new HashMap<String, IOrderFragment>();

	final ExistingOrderContext orderContext;

	final Currency currency;

	// add by lijun
	// 是否是确认视图
	boolean isConfirmView = false;

	public ExistingOrderComposite(ExistingOrderContext orderContext,
			Currency currency) {
		this.orderContext = orderContext;
		this.currency = currency;
	}

	public ExistingOrderContext getExistingOrderContext() {
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

	public boolean isConfirmView() {
		return isConfirmView;
	}

	public void setConfirmView(boolean isConfirmView) {
		this.isConfirmView = isConfirmView;
	}

}
