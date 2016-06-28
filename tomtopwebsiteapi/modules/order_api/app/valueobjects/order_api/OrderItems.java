package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;

public class OrderItems implements IOrderFragment,Serializable {
	private List<OrderItem> list;

	public OrderItems(List<OrderItem> list) {
		this.list = list;
	}

	public List<OrderItem> getList() {
		return list;
	}

	public void setList(List<OrderItem> list) {
		this.list = list;
	}

}
