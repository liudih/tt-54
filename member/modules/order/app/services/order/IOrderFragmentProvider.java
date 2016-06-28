package services.order;

import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.IOrderFragment;
import valueobjects.order_api.OrderContext;

public interface IOrderFragmentProvider {
	/**
	 * 用于尚未存在的订单，即主体为Cart的展示
	 * 
	 * @param context
	 * @return
	 */
	IOrderFragment getFragment(OrderContext context);

	/**
	 * 用于已存在的订单，即主体为Order的展示
	 * 
	 * @param context
	 * @return
	 */
	IOrderFragment getExistingFragment(ExistingOrderContext context);
}
