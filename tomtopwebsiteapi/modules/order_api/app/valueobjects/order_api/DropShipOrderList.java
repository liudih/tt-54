package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;

import dto.order.BillDetail;

public class DropShipOrderList implements Serializable {
	private DropShipOrderMessage dropShipOrderMessage;
	private String symbol;
	private List<OrderItem> orderItems;
	private List<BillDetail> orderBillDetails;

	public DropShipOrderMessage getDropShipOrderMessage() {
		return dropShipOrderMessage;
	}

	public void setDropShipOrderMessage(
			DropShipOrderMessage dropShipOrderMessage) {
		this.dropShipOrderMessage = dropShipOrderMessage;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public List<BillDetail> getOrderBillDetails() {
		return orderBillDetails;
	}

	public void setOrderBillDetails(List<BillDetail> orderBillDetails) {
		this.orderBillDetails = orderBillDetails;
	}
}