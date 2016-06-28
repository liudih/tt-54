package valueobjects.order_api.cart;

import java.util.List;

/**
 * 如果在向购物车添加各种优惠活动
 * 但是又想保证某些优惠活动是不能同时用
 * 那么你就需要实现该接口,然后把你实现类注入到系统里
 * 
 * 购物车ExtraLine rule
 * @author lijun
 *
 */
public interface IExtraLineRule {
	/**
	 * 需要做exclusive的插件id
	 * @return
	 */
	public String getPluginId();

	public RuleType getRuleType();
	
	/**
	 * 要排除的插件id
	 * @return
	 */
	public List<String> getExcludePluginIDs();
	
	public enum RuleType {
		/**
		 * 排除其他所有
		 */
		EXCLUDE_ALL,
		/**
		 * 排除部分其他
		 */
		EXCLUDE_PART;
	}
}
