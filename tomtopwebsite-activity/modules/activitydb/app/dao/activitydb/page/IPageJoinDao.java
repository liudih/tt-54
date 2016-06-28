package dao.activitydb.page;

import java.util.List;

import entity.activity.page.PageJoin;
import values.activity.page.PageJoinQuery;


/**
 * 参与活动Dao接口
 * @author Guozy
 *
 */
public interface IPageJoinDao {	
	
	/**
	 * 根据cemail分组，获取参与活动的所有数据信息的数量
	 * @return
	 */
	public Integer getPageJoinsBycjoinerAndIpageId(String cjoiner,
			Integer ipageid);
	
	List<PageJoin> getJoinedCount(int activityPageId, String memberID, int siteID);
	
	public int addPageJoin(PageJoin pageJoin);

	public int getJoinMemberCount(int activityPageId, int siteID);
}
