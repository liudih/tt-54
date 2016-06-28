package services.activity.page;

import java.util.List;

import valueobject.activity.page.PageItemName;


/**
 * 页面子项名称dao接口
 * @author liulj
 *
 */
public interface IPageItemNameService {
	
	PageItemName getById(int id);
	
	/**
	 * 根具页面子项Id获取标题
	 * @param pageid
	 * @return
	 */
	List<PageItemName> getListByPageItemid(Integer pageid);

	int insert(PageItemName pageItemName);

	int update(PageItemName pageItemName);

	int deleteByID(int id);
}
