package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;

import dto.order.Order;
import dto.order.OrderDetail;

public class ConfirmedOrder implements Serializable {
	private Order order;
	private List<OrderDetail> details;

	public ConfirmedOrder(Order order, List<OrderDetail> details) {
		this.order = order;
		this.details = details;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<OrderDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetail> details) {
		this.details = details;
	}

}
