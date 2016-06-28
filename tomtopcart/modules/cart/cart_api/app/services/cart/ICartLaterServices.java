package services.cart;

import java.util.List;

import valueobjects.cart.CartItem;
/**
 *************************************************************** 
 * <p>
 * @DESCRIPTION : 购物车服务的service接口
 * @AUTHOR : wenlong
 * @DATE :2015-10-06 上午11:35:33
 *</p>
 **************************************************************** 
 */
public interface ICartLaterServices {
	//添加临时保存
	public void addLaterItem(CartItem cartItem);
	//移除临时保存
	public void deleteLaterItem(List<CartItem> items);
	//更新临时保存
	public void updateLaterItemQty(CartItem item);
	//查询临时保存列表
	public List<CartItem> getAllLaterItems(Integer siteid, Integer lang,
			String currency);

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
	public void getItem(CartItem item);
}