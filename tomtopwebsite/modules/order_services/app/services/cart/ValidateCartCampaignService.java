package services.cart;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.google.inject.Singleton;

import facades.cart.Cart;
import play.Logger;
import valueobjects.order_api.cart.ExtraLine;

/**
 * 
 * @author lijun
 *
 */
@Singleton
public class ValidateCartCampaignService implements
		IValidateCartCampaignService {

	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	Set<IHandleCartRefreshEventPlugin> hcrep;

	@Override
	public boolean checkCampaign(String cartId) {
		Cart cart = cartLifecycle.getCart(cartId);
		Map<String, ExtraLine> el = cart.getExtraLines();
		if (el.size() > 0) {
			this.recheckCampaign(cart);
			return true;
		}

		return false;
	}

	/**
	 * 当购物车变动时再一次检查优惠活动是否满足规则 用户有可能会删除购物车里面的某个(或几个)产品,那么剩下的产品有肯能不满足优惠券的使用规则了
	 * 所以需要验证当前的购物车里的产品是否满足rule，不满足则把该优惠券移除
	 * 为什么没有用event来做,是应为event是异步的,而购物车里面的优惠券是要同步操作的
	 * 为什么没有用事件监听模式是因为事件监听模式是异步调用,而用户界面要实时显示,所以没有用事件监听模式
	 * 
	 * @author lijun
	 * @param cart
	 */
	private void recheckCampaign(Cart cart) {
		if (hcrep != null) {
			try {
				hcrep.forEach(c -> {
					c.handleCartRefreshEvent(cart);
				});
			} catch (Exception e) {
				Logger.error("handle cart refresh event failed", e);
			}
		}
	}
}
