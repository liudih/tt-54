package dto.order;

import dto.ShippingMethodDetail;
import dto.order.Order;
/**
 * 订单追踪的基本信息
 * @author Administrator
 *
 */
public class OrderTracking {	
	private Order order;
	
	private String orderName;
	
	private ShippingMethodDetail shippingMethodName;
	
	private String trackingnumber;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public ShippingMethodDetail getShippingMethodName() {
		return shippingMethodName;
	}

	public void setShippingMethodName(ShippingMethodDetail shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	public String getTrackingnumber() {
		return trackingnumber;
	}

	public void setTrackingnumber(String trackingnumber) {
		this.trackingnumber = trackingnumber;
	}
	
	
	
}
