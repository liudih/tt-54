package services.cart;

import java.util.List;

import com.google.common.collect.Multimap;

import dto.cart.CartItemList;
import valueobjects.cart.CartItem;

public interface ICartServices {

	public void addCartItem(CartItem cartItem);

	public void deleteItem(List<CartItem> items);

	public void updateItemQty(CartItem item);

	public List<CartItem> getAllItems(Integer siteid, Integer lang,
			String currency);
	
	public void deleteAllItem();

	/**
	 * Saved for later
	 * 
	 * @author lijun
	 * @param item
	 */
	public void saveForlater(CartItem item);

	/**
	 * 获取购物车里面的单个商品
	 * 
	 * @author lijun
	 * @param item
	 */
	public CartItem getItem(CartItem item);
	
	/**
	 * 判断库存是否足够
	 * @param itemID
	 * @param qty
	 * @param listingids
	 * @return
	 */
	public abstract boolean isEnoughQty(String itemID, Integer qty,
			List<String> listingids);
	
	/**
	 * 获取当前仓库id的cartitems
	 * @author xiaoch
	 */
	public List<CartItem> getAllItemsCurrentStorageid(Integer siteid, Integer lang,
			String currency);

}