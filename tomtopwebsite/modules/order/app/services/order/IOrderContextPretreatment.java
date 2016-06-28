package services.order;

import valueobjects.order_api.ExistingOrderContext;
import valueobjects.order_api.OrderContext;

/**
 * 实现此接口并进行注册，将对OrderContext进行预处理 届时，所有的fragment provider
 * 将访问你处理后并存放在OrderContext中的数据
 * 
 * @author Luo JH
 *
 */
public interface IOrderContextPretreatment {

	/**
	 * 主体为Cart，接收OrderContext，处理并返回OrderContext
	 * 
	 * @param context
	 * @return
	 */
	OrderContext pretreatmentContext(OrderContext context);

	/**
	 * 主体为Order
	 * 
	 * @param context
	 * @return
	 */
	ExistingOrderContext pretreatExstingOrderContext(
			ExistingOrderContext context);

}
