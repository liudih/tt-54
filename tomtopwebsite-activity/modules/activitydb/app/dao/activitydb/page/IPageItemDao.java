package dao.activitydb.page;

import java.util.List;

import values.activity.page.PageItemQuery;
import entity.activity.page.PageItem;


/**
 * 页面子项Dao接口类
 * 
 * @author liulj
 */
public interface IPageItemDao {
	public PageItem getById(int id);
	/**
	 * 获取总数，配合getPage用的
	 * @param ienable
	 * @param url
	 * @param type
	 * @return
	 */
	int getCount(Integer ienable,String url,Integer type);

	/**
	 * 获取分页数据
	 * @param page 页码
	 * @param pagesize 页面大小
	 * @param ienable  查询条件是否启用
	 * @param url 查询条件主题url
	 * @param iid 页面类型
	 * @return
	 */
	List<PageItemQuery> getPage(int page, int pagesize,Integer ienable,String url,Integer type);
	/**
	 * 添加子项
	 * 
	 * @param page
	 * @return
	 */
	public int add(PageItem pageItem);

	/**
	 * 根据专题编号，删除子项
	 * 
	 * @param iid
	 * @return
	 */
	public int deleteByIid(int iid);

	/**
	 * 根据页面编号，修改子项
	 * 
	 * @param page
	 * @return
	 */
	public int updateByIid(PageItem pageItem);
	/**
	 * 获取所有的数据
	 * 
	 * @return
	 */
	public List<PageItem> getAll();

	/**
	 * 
	 * @Title: getPageItemByPageId
	 * @Description: TODO(通过活动id获取活动项目)
	 * @param @param iid
	 * @param @return
	 * @return List<PageItem>
	 * @throws 
	 * @author yinfei
	 */
	List<PageItem> getPageItemByPageId(Integer iid);
}
