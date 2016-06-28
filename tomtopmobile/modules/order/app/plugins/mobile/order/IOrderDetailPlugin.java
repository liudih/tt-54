package plugins.mobile.order;

import play.twirl.api.Html;
import dto.order.Order;

/**
 * 订单详细
 * 
 * @author lijun
 *
 */
public interface IOrderDetailPlugin {
	String getName();

	/**
	 * 负责绘制OrderFragment
	 * 
	 * @param context
	 * @author lijun
	 * @return
	 */
	Html render(Order order);
}