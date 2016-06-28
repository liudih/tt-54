package extensions.order.shipping;

import java.util.List;

import valueobjects.order_api.ShippingMethodInformation;
import valueobjects.order_api.shipping.ShippingMethodRequst;

public interface IFreightPlugin {
	/**
	 * 处理过程
	 * 
	 * @param list
	 *            滤后的List<ShippingMethodInformation>
	 * @param requst
	 * @return 返回处理后的List<ShippingMethodInformation>
	 */
	public List<ShippingMethodInformation> processing(
			List<ShippingMethodInformation> list, ShippingMethodRequst requst);

	/**
	 * 处理顺序，越大越后处理
	 * 
	 * @return
	 */
	public int order();
}