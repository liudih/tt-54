package valueobjects.order_api;
import java.io.Serializable;
import java.util.List;

import dto.Currency;
import dto.order.BillDetail;
import dto.order.Order;

public class OrderList implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Order order;
	private String symbol;
	private List<OrderItem> orderItems;
	private List<BillDetail> orderBillDetails;
	//add by lijun
	private Currency currency;
	
	//dropshipping
	private double grandprice;
	private List<Order> orders;
	private String dropshippingId;
	
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
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
	public double getGrandprice() {
		return grandprice;
	}
	public void setGrandprice(double grandprice) {
		this.grandprice = grandprice;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public String getDropshippingId() {
		return dropshippingId;
	}
	public void setDropshippingId(String dropshippingId) {
		this.dropshippingId = dropshippingId;
	}
	
}