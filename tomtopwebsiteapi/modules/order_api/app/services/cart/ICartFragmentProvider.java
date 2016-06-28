package services.cart;

import java.util.Map;

import valueobjects.order_api.cart.CartContext;
import valueobjects.order_api.cart.ICartFragment;
import valueobjects.product.ProductComposite;

public interface ICartFragmentProvider {

	/**
	 *
	 * @param context
	 *            购物车的查找条件
	 * @param processingContext
	 *            在准备过程中，有特别数据要返回的，可以用此对象
	 * @return
	 * @see ProductComposite#getAttributes()
	 */
	ICartFragment getFragment(CartContext context,
			Map<String, Object> processingContext);
}
