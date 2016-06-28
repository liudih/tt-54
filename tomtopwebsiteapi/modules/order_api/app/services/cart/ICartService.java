package services.cart;

import java.util.List;
import java.util.Map;

import valueobjects.order_api.ExtraLineView;
import valueobjects.order_api.cart.CartItem;
import valueobjects.order_api.cart.CartOwner;

import com.google.common.collect.Multimap;

import dto.cart.CartItemList;
import facades.cart.Cart;

public interface ICartService {

	public abstract List<CartItem> getCartItemsByCartId(String cartId,
			int siteID, int languageID, String ccy, boolean generateorders);

	public abstract List<CartItem> getCartItemsLiteByCartId(String cartId);

	public abstract boolean delCartByCid(String id);

	public abstract void delCartItemByCartID(String id);

	public abstract boolean addItemHistory(List<String> listingIDS,
			String email, String uuid);

	public abstract Map<String, Object> addItem(String cartId, CartItem cartItem);

	public abstract Map<String, Object> addItemQty(String cartItemID,
			Integer qty);

	public abstract Map<String, Object> updateItemQty(String itemID, int qty);

	/**
	 * 判断库存是否足够
	 * 
	 * @param itemID
	 * @param qty
	 * @param listingids
	 * @return
	 */
	public abstract boolean isEnoughQty(String itemID, Integer qty,
			List<String> listingids);

	public abstract Map<String, Object> getPriceByCartItemId(String id, int qty);

	public abstract CartItem convertFromEntity(dto.cart.CartItem ci,
			Multimap<String, CartItemList> cartItemListIndex, String ccy);

	public abstract CartItem convertFromEntityLite(dto.cart.CartItem ci,
			Multimap<String, CartItemList> cartItemListIndex);

	/**
	 * 判断cartId与email是否匹配，且未生成订单
	 *
	 * @param email
	 * @param cartId
	 * @return
	 * @author luojiaheng
	 */
	public abstract boolean validCart(String email, String cartId);

	/**
	 * 检查CartID是否可以Checkout
	 * <ul>
	 * <li>是否已经成功生成订单
	 * <li>是否属于会员（登入、升级后的Cart）
	 * </ul>
	 * 
	 * @param cartId
	 * @return
	 */
	public abstract boolean readyForCheckout(String cartId);

	public abstract CartOwner getCartOwner(String cartId);

	public abstract boolean deleteCartItemList(String cartItemID,
			String listingID);

	public abstract boolean deleteMainItemList(String cartItemID,
			String listingID, Cart cart);

	public abstract boolean checkItemQty(CartItem ci, Integer qty);

	public abstract boolean checkItemListQty(CartItem ci, Integer qty);

	public abstract List<ExtraLineView> convertExtras(facades.cart.Cart ct);

	public abstract int updateCartIsCreateOrder(String cartId);

}