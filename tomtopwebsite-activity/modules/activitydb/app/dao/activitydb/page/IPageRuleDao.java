package dao.activitydb.page;

import java.util.List;

import values.activity.page.PageRuleQuery;
import entity.activity.page.PageRule;

/**
 * 页面抽奖规则dao
 * 
 * @author liulj
 */
public interface IPageRuleDao {
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
	List<PageRuleQuery> getPage(int page, int pagesize, String url);

	public int add(PageRule qualification);

	public int deleteByid(int iid);

	public int updateByIid(PageRule qualification);

	public PageRule getById(Integer id);

	int getCountByPageid(Integer pageid);

	List<PageRule> getListByPageid(int pageid);
	
	/**
	 * 
	 * @Title: getPageRuleByPageId
	 * @Description: TODO(通过活动id查询活动规则)
	 * @param @param pageId
	 * @param @return
	 * @return List<PageRule>
	 * @throws 
	 * @author yinfei
	 */
	List<PageRule> getPageRuleByPageId(int pageId);
}
