package services.order;

import play.twirl.api.Html;
import valueobjects.order_api.IOrderFragment;

public interface IOrderFragmentRenderer {
	/**
	 * 主体为Cart，展示Html
	 *
	 * @param fragment
	 * @param context
	 *            helpers to access other contents
	 * @return
	 */
	Html render(IOrderFragment fragment, OrderRenderContext context);

	/**
	 * 主体为Order
	 *
	 * @param fragment
	 * @param context
	 *            helpers to access other contents
	 * @return
	 */
	Html renderExisting(IOrderFragment fragment,
			ExistingOrderRenderContext context);
}
