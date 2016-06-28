package extensions.cart.member;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import mapper.cart.CartItemListMapper;
import mapper.cart.CartItemMapper;

import org.elasticsearch.common.collect.Collections2;
import org.elasticsearch.common.collect.Lists;

import play.Logger;
import services.IDefaultSettings;
import services.cart.ICartLifecycleService;
import services.cart.ICartService;
import valueobjects.order_api.cart.CartGetRequest;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.Inject;

import dto.cart.CartItem;
import dto.cart.CartItemList;
import events.member.LoginEvent;
import extensions.member.login.ILoginProcess;
import facades.cart.CartItemComparator;
import facades.cart.Cart;

public class MergeCartProcess implements ILoginProcess {
	@Inject
	ICartLifecycleService cartLifecycle;

	@Inject
	CartItemMapper cartBaseMapper;

	@Inject
	CartItemListMapper cartBaseListMapper;

	@Inject
	CartItemMapper cartItemMapper;

	@Inject
	ICartService cartService;

	@Inject
	IDefaultSettings defaultSettings;

	@Override
	public String getName() {
		return "merge-cart";
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public String execute(LoginEvent event) {
		Logger.debug("meger cart==================");
		try {
			Logger.debug(
					"CartUserUpdateHandler(LoginEvent: uuid={}, email={})",
					event.getLTC(), event.getEmail());

			CartGetRequest req = new CartGetRequest(event.getEmail(),
					event.getLTC(), event.getSiteID(),
					defaultSettings.getDefaultLanguageID(),
					defaultSettings.getDefaultCurrency());
			Cart cart = cartLifecycle.getOrCreateCart(req);

			List<CartItemList> cartItemList = cartBaseListMapper
					.getCartItemListByUUID(event.getLTC(), event.getSiteID());
			List<CartItem> cartItem = cartItemMapper.getCartItemsByUUID(event
					.getLTC(), event.getSiteID());

			Multimap<String, CartItemList> cartItemListIndex = Multimaps.index(
					cartItemList, cilist -> cilist.getCcartitemid());
			Map<String, dto.cart.CartItem> cartItemIndex = Maps.uniqueIndex(
					cartItem, i -> i.getCid());
			Map<String, valueobjects.order_api.cart.CartItem> result = Maps
					.transformValues(cartItemIndex, ci -> cartService
							.convertFromEntityLite(ci, cartItemListIndex));
			List<valueobjects.order_api.cart.CartItem> resultItems = Lists
					.newArrayList(result.values());
			Logger.debug("nologin resultItems ===={}", resultItems.size());
			List<valueobjects.order_api.cart.CartItem> existingItems = cartService
					.getCartItemsLiteByCartId(cart.getId());
			Logger.debug("login existingItems ===={}", existingItems.size());
			CartItemComparator cc = new CartItemComparator();
			for (valueobjects.order_api.cart.CartItem ct : resultItems) {
				Logger.debug("nologin cartitemcid ===={}", ct.getCid());
				Collection<valueobjects.order_api.cart.CartItem> dups = Collections2
						.filter(existingItems, i -> cc.compare(i, ct) == 0);
				Logger.debug("Dups size: {}", dups.size());
				if (dups.size() == 0) {
					cartBaseMapper.updateItemForTransCart(ct.getCid(),
							cart.getId());
				} else if (dups.size() > 0) {
					valueobjects.order_api.cart.CartItem ci = dups.iterator()
							.next();
					Logger.debug("exist cartitem ===={}", ct.getCid());
					if (ct.getIqty() > ci.getIqty()) {
						cartService.updateItemQty(ci.getCid(), ct.getIqty());
					}
					cartService.delCartByCid(ct.getCid()); // 清空未登录时的购物车内容
				}
			}
			return "success";
		} catch (Exception e) {
			Logger.error("Cart Upgrade Exception", e);
			return "error";
		}
	}
}
