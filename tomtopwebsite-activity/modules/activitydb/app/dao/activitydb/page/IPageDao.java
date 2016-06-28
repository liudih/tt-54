package dao.activitydb.page;

import java.util.Date;
import java.util.List;

import dto.image.Img;
import dto.member.MemberBase;
import dto.order.Order;
import entity.activity.page.Page;
import values.activity.page.PageQuery;

/**
 * 页面Dao接口类
 * 
 * @author liulj
 */
public interface IPageDao {
	/**
	 * 获取总数，配合getPage用的
	 * 
	 * @param ienable
	 * @param url
	 * @param type
	 * @return
	 */
	int getCount(Integer ienable, String url, Integer type);

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页面大小
	 * @param ienable
	 *            查询条件是否启用
	 * @param url
	 *            查询条件主题url
	 * @param iid
	 *            页面类型
	 * @return
	 */
	List<PageQuery> getPage(int page, int pagesize, Integer ienable,
			String url, Integer type);

	/**
	 * 添加页面
	 * 
	 * @param page
	 * @return
	 */
	public int add(Page page);

	/**
	 * 根据专题编号，删除页面
	 * 
	 * @param iid
	 * @return
	 */
	public int deleteByIid(int iid);

	/**
	 * 根据页面编号，修改页面
	 * 
	 * @param page
	 * @return
	 */
	public int updateByIid(Page page);

	/**
	 * 获取所有的数据
	 * 
	 * @return
	 */
	public List<Page> getAll();

	public Page getById(Integer id);

	/**
	 * 验证主题url是存在
	 * 
	 * @param url
	 * @return 0 表示不存，大于1表示存在
	 */
	public int validateUrl(String url);

	/**
	 * 
	 * @Title: getPageByUrl
	 * @Description: TODO(通过url查询活动页面)
	 * @param @param title
	 * @param @param websiteId
	 * @param @return
	 * @return Page
	 * @throws 
	 * @author yinfei
	 */
	Page getPageByUrl(String title, int websiteId);

}
