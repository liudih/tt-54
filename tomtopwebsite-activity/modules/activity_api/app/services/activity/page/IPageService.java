package services.activity.page;

import java.util.List;

import valueobject.activity.page.Page;
import forms.activity.page.PageForm;

/**
 * 主题服务接口
 * 
 * @author liulj
 *
 */
public interface IPageService {
	List<Page> getAll();

	Page getById(int id);

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 *            页码
	 * @param pageSize
	 *            页面大小
	 * @param themeQuery
	 *            查询条件
	 * @return
	 */
	valueobjects.base.Page<PageForm> getPage(int page, int pageSize,
			PageForm themeQuery);

	/**
	 * 插入页面的信息
	 * 
	 * @param pageForm
	 * @return
	 */
	int insertInfo(PageForm pageForm);

	/**
	 * 更新页面的信息
	 * 
	 * @param pageForm
	 * @return
	 */
	int updateInfo(PageForm pageForm);

	int deleteByID(int id);

	/**
	 * 验证主题url是存在
	 * 
	 * @param url
	 * @return 0 表示不存，大于1表示存在
	 */
	public int validateUrl(String url);


}
