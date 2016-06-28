package services.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import valueobjects.order_api.cart.CartItem;
import dto.Country;
import dto.ShippingMethodDetail;
import facades.cart.Cart;

public interface IFreightService {

	/**
	 * 
	 * @param cart
	 * @param isNeedFreeShippingLabel
	 *            是否判断免邮标签
	 * @return
	 */
	public abstract Double getTotalWeight(Cart cart,
			Boolean isNeedFreeShippingLabel);

	/**
	 * 
	 * @param map
	 *            listingID为key，qty为值
	 * @param isNeedFreeShippingLabel
	 * @return
	 */
	public abstract Double getTotalWeight(Map<String, Integer> map,
			Boolean isNeedFreeShippingLabel);

	public abstract Double getTotalWeight(Cart cart);

	/**
	 *
	 * @param shippingMethodId
	 * @param weight
	 *            (Units: g)
	 * @param shippingWeight
	 *            去掉免邮产品后的重量
	 * @param exchangeRate
	 * @return
	 * @author luojiaheng
	 */
	public abstract Double getFinalFreight(Integer shippingMethodId,
			Double weight, Double shippingWeight, String curency, Integer lang,
			double grandTotal);

	/**
	 * 获取最终运费（进过判断是否免费之后的价格）
	 *
	 * @param shippingMethod
	 * @param weight
	 *            (单位：克)
	 * @param shippingWeight
	 *            去掉免邮产品后的重量
	 * @param exchangeRate
	 * @param hasAllFreeShipping 是否包含全部免邮产品
	 * @return
	 * @author luojiaheng
	 */
	public abstract Double getFinalFreight(ShippingMethodDetail shippingMethod,
			Double weight, Double shippingWeight, String currency,
			double grandTotal,boolean hasAllFreeShipping);

	/**
	 * 根据super rule计算
	 *
	 * @param shippingMethod
	 * @param weight
	 * @param exchangeRate
	 * @return
	 * @author luojiaheng
	 */
	public abstract Double getFreight(ShippingMethodDetail shippingMethod,
			Double weight, String currency);

	/**
	 * 使用java内置的js脚本, 计算运费
	 * 
	 * @param weight
	 *            重量(g)
	 * @param exchangeRate
	 *            对美元汇率，如果空，则默认从接口中取得当前货币汇率
	 *
	 * @return Double型的运费 或者null, 无法运算
	 * */
	public abstract Double getFirstFreight(ShippingMethodDetail shippingMethod,
			Double weight);

	public abstract Double runJS(HashMap<String, String> subs, String ruleValue);

	/**
	 * 计算总重量(未排除免邮重量)
	 * 
	 * @author lijun
	 * @param items
	 * @return
	 */
	public Double getTotalWeight(List<CartItem> items);

	/**
	 * @author lijun
	 * @param items
	 * @return
	 */
	public Double getTotalWeightV2(List<valueobjects.cart.CartItem> items);

	/**
	 * 计算总重量(排除免邮)
	 * 
	 * @author lijun
	 * @param items
	 * @return
	 */
	public Double getTotalShipWeight(List<CartItem> items);

	/**
	 * @author lijun
	 * @param items
	 * @return
	 */
	public Double getTotalShipWeightV2(List<valueobjects.cart.CartItem> items);

	/**
	 * @author lijun
	 * @param weight
	 * @param shippingWeight
	 * @param shippingMethod
	 * @param country
	 * @param baseTotal
	 * @param currency
	 * @param websiteId
	 * @param listingIDs
	 * @return
	 */
	public Double getDoubleFreight(Double weight, Double shippingWeight,
			ShippingMethodDetail shippingMethod, Country country,
			double baseTotal, String currency, int websiteId,
			List<String> listingIDs);

}