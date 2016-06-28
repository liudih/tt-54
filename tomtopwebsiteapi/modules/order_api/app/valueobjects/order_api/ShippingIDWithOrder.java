package valueobjects.order_api;

public class ShippingIDWithOrder {
	private Integer shippingID;
	private Integer orderID;

	public Integer getShippingID() {
		return shippingID;
	}

	public void setShippingID(Integer shippingID) {
		this.shippingID = shippingID;
	}

	public Integer getOrderID() {
		return orderID;
	}

	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}

}
