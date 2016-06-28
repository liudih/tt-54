package dao.activitydb.page;

import java.util.List;

import entity.activity.page.PageItemName;

/**
 * 页面子项名称dao接口
 * @author liulj
 *
 */
public interface IPageItemNameDao {
	List<PageItemName> getAll();
	
	PageItemName getById(int id);
	
	/**
	 * 根具页面子项Id获取标题
	 * @param pageid
	 * @return
	 */
	List<PageItemName> getListByPageItemid(Integer pageid);

	int insert(PageItemName PageItemName);

	int update(PageItemName PageItemName);

	int deleteByID(int id);

	/**
	 * 
	 * @Title: getPINameByPIIdAndLId
	 * @Description: TODO(通过活动项目id和语言id查询活动项目名称)
	 * @param @param iid
	 * @param @param languageId
	 * @param @return
	 * @return String
	 * @throws 
	 * @author yinfei
	 */
	String getPINameByPIIdAndLId(Integer iid, int languageId);
}
