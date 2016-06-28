package services.order;

import java.util.List;

import dto.Country;
import dto.ShippingMethodDetail;
import valueobjects.cart.CartItem;
import valueobjects.loyalty.LoyaltyPrefer;

/**
 * 结算服务类
 * 
 * @author lijun
 *
 */
public interface ICheckoutService {

	/**
	 * 单纯获取商品的subtotal
	 * 
	 * @param items
	 * @return
	 */
	public Double subToatl(List<CartItem> items);

	/**
	 * 最后结算
	 * 
	 * @param items
	 * @param shippingMethod
	 *            快递方式
	 * @param shipToCountry
	 *            邮寄的国家
	 * @return
	 */
	public Double sum(List<CartItem> items,
			ShippingMethodDetail shippingMethod, Country ShipToCountry,List<LoyaltyPrefer> prefers,
			String currency);

}
