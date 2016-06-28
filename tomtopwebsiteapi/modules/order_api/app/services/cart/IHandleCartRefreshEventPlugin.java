package services.cart;

import facades.cart.Cart;

/**
 * 当购物车刷新时会调用 该类是为了解决相互依赖
 * 
 * @author lijun
 *
 */
public interface IHandleCartRefreshEventPlugin {

	/**
	 * 处理购物车刷新事件,此方法只给pc端调用
	 * @param cart
	 */
	public void handleCartRefreshEvent(Cart cart);
}
