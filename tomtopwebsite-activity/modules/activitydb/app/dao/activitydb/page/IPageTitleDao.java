package dao.activitydb.page;

import java.util.List;

import entity.activity.page.PageTitle;

/**
 * 页面标题dao接口
 * @author liulj
 *
 */
public interface IPageTitleDao {
	List<PageTitle> getAll();
	
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

	/**
	 * 
	 * @Title: getPTByPageIdAndLId
	 * @Description: TODO(通过活动id和语言id查询活动标题)
	 * @param @param iid
	 * @param @param languageId
	 * @param @return
	 * @return PageTitle
	 * @throws 
	 * @author yinfei
	 */
	PageTitle getPTByPageIdAndLId(Integer iid, int languageId);
}
