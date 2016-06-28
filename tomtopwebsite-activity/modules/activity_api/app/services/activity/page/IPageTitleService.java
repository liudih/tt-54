package services.activity.page;

import java.util.List;

import valueobject.activity.page.PageTitle;

/**
 * 页面标题dao接口
 * @author liulj
 *
 */
public interface IPageTitleService {
	
	PageTitle getById(int id);
	
	/**
	 * 根具页面Id获取标题
	 * @param pageid
	 * @return
	 */
	List<PageTitle> getListByPageid(Integer pageid);

	int insert(PageTitle pageTitle);

	int update(PageTitle pageTitle);

	int deleteByID(int id);
	
	PageTitle getPTByPageIdAndLId(Integer iid, int languageId);
}
