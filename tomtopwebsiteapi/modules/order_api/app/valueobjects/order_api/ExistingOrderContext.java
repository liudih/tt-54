package valueobjects.order_api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import dto.Country;
import dto.order.Order;
import dto.order.OrderDetail;

public class ExistingOrderContext implements Serializable {
	private Order order;
	private List<OrderDetail> details;
	private Integer storageId;
	private Country country;
	private boolean isSelect;

	private HashMap<String, Object> attribute = new HashMap<String, Object>();

	public ExistingOrderContext(Order order, List<OrderDetail> details,
			boolean isSelect) {
		this.order = order;
		this.details = details;
		this.isSelect = isSelect;
	}

	public Object get(String key) {
		return attribute.get(key);
	}

	public void put(String key, Object value) {
		attribute.put(key, value);
	}

	public Integer getStorageId() {
		return storageId;
	}

	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}

	public HashMap<String, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(HashMap<String, Object> attribute) {
		this.attribute = attribute;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
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

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}