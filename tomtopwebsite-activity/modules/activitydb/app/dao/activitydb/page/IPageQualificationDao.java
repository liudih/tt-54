package dao.activitydb.page;

import java.util.List;

import values.activity.page.PageQualificationQuery;
import entity.activity.page.PageQualification;

/**
 * 页面访问规则dao
 * 
 * @author liulj
 */
public interface IPageQualificationDao {
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
	List<PageQualificationQuery> getPage(int page, int pagesize, String url);

	public int add(PageQualification qualification);

	public int deleteByIid(int iid);

	public int updateByIid(PageQualification qualification);

	public PageQualification getById(Integer id);

	/**
	 * 
	 * @Title: getPQByPageId
	 * @Description: TODO(通过活动id获取筛选条件)
	 * @param @param pageId
	 * @param @return
	 * @return List<PageQualification>
	 * @throws 
	 * @author yinfei
	 */
	List<PageQualification> getPQByPageId(int pageId);
}
