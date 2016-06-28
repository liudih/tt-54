package plugins.mobile.order.provider;

import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;

/**
 * 订单数据提供者
 * 
 * @author lijun
 *
 */
public interface IOrderFragmentProvider {
	/**
	 *
	 * @param context
	 *            订单相关信息查找条件
	 * @return
	 */
	IOrderFragment getFragment(OrderContext context);

}
