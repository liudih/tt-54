package valueobjects.order_api;

import java.util.List;
import java.util.Map;

import dto.order.FullPreparatoryOrder;

public class PreparatoryOrderListVO implements IOrderFragment {
	private List<FullPreparatoryOrder> orders;
	private Map<Integer, ShippingMethodInformations> shippingInfoMap;
	private Map<Integer, Double> subTotalMap;
	private double allSubTotal;

	public List<FullPreparatoryOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<FullPreparatoryOrder> orders) {
		this.orders = orders;
	}

	public Map<Integer, ShippingMethodInformations> getShippingInfoMap() {
		return shippingInfoMap;
	}

	public void setShippingInfoMap(
			Map<Integer, ShippingMethodInformations> shippingInfoMap) {
		this.shippingInfoMap = shippingInfoMap;
	}

	public Map<Integer, Double> getSubTotalMap() {
		return subTotalMap;
	}

	public void setSubTotalMap(Map<Integer, Double> subTotalMap) {
		this.subTotalMap = subTotalMap;
	}

	public double getAllSubTotal() {
		return allSubTotal;
	}

	public void setAllSubTotal(double allSubTotal) {
		this.allSubTotal = allSubTotal;
	}

}
