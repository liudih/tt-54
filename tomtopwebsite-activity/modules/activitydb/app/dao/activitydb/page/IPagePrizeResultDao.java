package dao.activitydb.page;

import java.util.List;

import entity.activity.page.PagePrize;
import entity.activity.page.PagePrizeResult;
import valueobjects.base.activity.result.PrizeResult;
import values.activity.page.PagePrizeResultQuery;

/**
 * 页面奖品统计报表dao接口
 * 
 * @author Guozy
 *
 */
public interface IPagePrizeResultDao {

	/**
	 * 获取页面奖品的统计报表的数据信息
	 * 
	 * @return
	 */
	public List<PagePrizeResultQuery> getPagePrizeResultsByDCreateDateAndAcitvityName(
			PagePrizeResultQuery pagePrizeResultQuery);

	/**
	 * 根据相应条件，获取页面数据数量条数
	 * 
	 * @param pagePrizeResultForm
	 * @return
	 */
	public int getPagePrizeResultsCount(
			PagePrizeResultQuery pagePrizeResultQuery);

	/**
	 * 根据相应条件,获取中奖统计报表的数据信息
	 * 
	 * @return
	 */
	public List<PagePrizeResultQuery> getPagePrizeResultsByDateAndNameAndPrizeAndEmailAndCountry(
			PagePrizeResultQuery pagePrizeResultQuery);

	/**
	 * 根据相应条件，获取中奖页面数据数量条数
	 * 
	 * @param pagePrizeResultForm
	 * @return
	 */
	public int getPagePrizeLotteryCount(
			PagePrizeResultQuery pagePrizeResultQuery);

	public List<PrizeResult> getPrizeResultByPageId(int pageId, int website);

	public List<PagePrizeResult> getPrizeResultByPIdAndMId(int activityPageId, String memberID, int pid);
}
