package services.order;

import java.util.List;

public interface IOrderTaggingService {

	public abstract void tag(int orderId, List<String> tags);

	public abstract void untag(int orderId, List<String> tags);

	/**
	 * 返回订单的Tags
	 * 
	 * @param orderId
	 * @return
	 */
	public abstract List<String> getOrderTags(int orderId);

	/**
	 * 返回订单ID
	 * 
	 * @param tags
	 *            全部需要存在的标签
	 * @return
	 */
	public abstract List<Integer> findContainAllTags(List<String> tags);

	/**
	 * 返回订单ID
	 * 
	 * @param tags
	 *            这里的标签只需要部分存在
	 * @return
	 */
	public abstract List<Integer> findContainAnyTags(List<String> tags);

}