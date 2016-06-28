package services.activity.page;
import java.util.List;

import valueobject.activity.page.PageItem;
import forms.activity.page.PageItemForm;

/**
 * 页面子项Dao接口类
 * 
 * @author liulj
 */
public interface IPageItemService {
	public PageItem getById(int id);
	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pagesize
	 *            页面大小
	 * @return
	 */
	valueobjects.base.Page<PageItemForm> getPage(int page, int pagesize,
			PageItemForm itemForm);

	/**
	 * 添加子项
	 * 
	 * @param page
	 * @return
	 */
	public int insertInfo(PageItemForm pageItem);

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
	public int updateInfo(PageItemForm pageItem);

	/**
	 * 获取页面子项的所有数据信息
	 * 
	 * @return
	 */
	public List<PageItem> getPageItems();

}
