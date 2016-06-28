package valueobjects.order_api;

import java.io.Serializable;
import java.util.List;

public class ShippingMethodInformations implements IOrderFragment, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ShippingMethodInformation> list;

	public ShippingMethodInformations(List<ShippingMethodInformation> list) {
		this.list = list;
	}

	public List<ShippingMethodInformation> getList() {
		return list;
	}

	public void setList(List<ShippingMethodInformation> list) {
		this.list = list;
	}
}
