package valueobjects.paypal_api;

/**
 * 支付状态接口
 * 
 * @author lijun
 *
 */
public interface IPaymentStatus{
	/**
	 * 获取站点id
	 * 
	 * @return
	 */
	public Integer getWebSiteId();

	/**
	 * 获取订单号
	 * 
	 * @return
	 */
	public String getOrderNum();

	/**
	 * 订单是否支付成功,该参数只代表paypal支付的时候没有errorCode返回(该参数=true 不代表钱已经到账,可能是pending)
	 * 
	 * @return
	 */
	public boolean isCompleted();

	/**
	 * 获取支付失败原因,失败消息会返回前台用户
	 * 
	 * @return
	 */
	public String getFailedInfo();
}
