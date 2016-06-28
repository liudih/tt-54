package services.cart;

/**
 * 该类是为了给客户端调用 服务端不要调用该方法 等待后面把优惠券使用放到订单那步后该类会被删除
 * 当用户在客户端操作了购物车数量，则需要调用该服务来验证已经在购物车里的优惠是否还满足规则
 * 
 * @author lijun
 *
 */
public interface IValidateCartCampaignService {

	/**
	 * 
	 * @param cartId
	 *            购物车id
	 * @return true ：已经放到购物车里的优惠券有变动(被移除) false : 未变动
	 */
	public boolean checkCampaign(String cartId);
}
