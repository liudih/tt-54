package plugins;


import play.twirl.api.Html;
import valueobjects.order_api.OrderContext;

/**
 * copy from website
 * 
 * @author renyy
 *
 */
public interface IOrderFragmentPlugin {
	String getName();

	/**
	 * 负责绘制OrderFragment
	 * 
	 * @param context
	 * @author lijun
	 * @return
	 */
	Html render(OrderContext context);
}