package extensions.order;

import play.twirl.api.Html;

/**
 * 推广活动用户UI界面提供者
 * 
 * @author lijun
 *
 */
public interface CampaignUiProvider {
	/**
	 * 通过该id来获取购物车里面的ExtraLine 该id请和服务端的id保持一致
	 * 
	 * @return
	 */
	public String getExtraLineId();

	/**
	 * provider name
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 绘制优惠活动使用UI
	 * 
	 * @return
	 */
	public Html render(CampaignUiContext ctx);

	/**
	 * 绘制ExtraLine
	 * 
	 * @param ctx
	 * @return
	 */
	public Html renderExtraLine(CampaignUiContext ctx);
}
