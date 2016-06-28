package service.activity;

import java.util.Date;
import java.util.List;

import values.activity.page.PageItemCount;

public interface IVoteRecordService {

	public int getPageItemCount(int itemid, int websiteid);

	public List<PageItemCount> getPageAllItemCount(int pageId, int websiteid);
	
	public int getUserPageItemCount(String email, int pageid, int websiteid);

	/**
	 * 
	 * @Title: getPageItemCountToday
	 * @Description: TODO(查询投票记录通过站点、用户名、投票项、当前日期、明天日期)
	 * @param @param websiteId
	 * @param @param memberID
	 * @param @param activityPageItemId
	 * @param @param date
	 * @param @param tomorrowDate
	 * @param @return
	 * @return int
	 * @throws 
	 * @author yinfei
	 */
	public int getPageItemCountToday(int websiteId, String memberID,
			int activityPageItemId, Date date, Date tomorrowDate);
}
