package events.order;

import valueobjects.order_api.OrderValue;

public class OrderCancelledEvent {

	private final OrderValue orderValue;
	private final Integer languageId;

	public OrderCancelledEvent(OrderValue orderValue, Integer languageId) {
		this.orderValue = orderValue;
		this.languageId = languageId;
	}

	public OrderValue getOrderValue() {
		return orderValue;
	}

	public Integer getLanguageId() {
		return languageId;
	}

}
