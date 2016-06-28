package dao.activitydb.page;

import java.util.List;

import values.activity.page.PagePrizeQuery;
import entity.activity.page.PagePrize;

/**
 * 页面抽奖奖品dao
 * 
 * @author liulj
 */
public interface IPagePrizeDao {
	/**
	 * 获取总数，配合getPage用的
	 * 
	 * @param url
	 * @return
	 */
	int getCount(String url);

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页面大小
	 * @param url
	 *            查询条件页面url
	 * @return
	 */
	List<PagePrizeQuery> getPage(int page, int pagesize, String url);

	int deleteByRuleid(Integer ruleid);

	public int add(PagePrize prize);

	public int deleteByid(int iid);

	public int updateById(PagePrize prize);

	public PagePrize getById(Integer id);

	/**
	 * 获取奖品的所有数据信息
	 * 
	 * @return
	 */
	public List<PagePrize> getPagePrizes();
	
	/**
	 * 
	 * @Title: getPPByPageId
	 * @Description: TODO(通过活动id查询活动结果)
	 * @param @param pageId
	 * @param @return
	 * @return List<PagePrize>
	 * @throws 
	 * @author yinfei
	 */
	public List<PagePrize> getPPByPageId(int pageId);
}
