package services.activity.page;

import java.util.List;

import forms.activity.page.PagePrizeResultForm;

/**
 * 页面奖品服务类接口
 * 
 * @author Administrator
 *
 */
public interface IPagePrizeResultService {

	/**
	 * 根据相应条件,获取页面奖品的统计报表的数据信息
	 * 
	 * @return
	 */
	public List<PagePrizeResultForm> getPagePrizeResultsByDCreateDateAndAcitvityName(
			PagePrizeResultForm pagePrizeResultForm);

	/**
	 * 根据相应条件，获取页面数据数量条数
	 * 
	 * @param pagePrizeResultForm
	 * @return
	 */
	public int getPagePrizeResultsCount(PagePrizeResultForm pagePrizeResultForm);

	/**
	 * 根据相应条件,获取中奖统计报表的数据信息
	 * 
	 * @return
	 */
	public List<PagePrizeResultForm> getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(
			PagePrizeResultForm pagePrizeResultForm);

	/**
	 * 根据相应条件，获取中奖页面数据数量条数
	 * 
	 * @param pagePrizeResultForm
	 * @return
	 */
	public int getPagePrizeLotteryCount(PagePrizeResultForm pagePrizeResultForm);

}
