package dao.activitydb.page;

import java.util.Date;
import java.util.List;

import values.activity.page.PageItemCount;
import values.activity.page.VoteRecordQuery;
import entity.activity.page.VoteRecord;

public interface IVoteRecordDao {

	int deleteByPrimaryKey(Integer iid);

	int insert(VoteRecord record);

	VoteRecord selectByPrimaryKey(Integer iid);

	int updateByPrimaryKey(VoteRecord record);

	int getPageItemCount(int itemid, int websiteid);

	List<PageItemCount> getPageAllItemCount(int pageId, int websiteid);

	int getUserPageItemCount(String email, int pageid, int websiteid);

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
	int getPageItemCountToday(int websiteId, String memberID,
			int activityPageItemId, Date date, Date tomorrowDate);
	 
	/**
	 * 根据相应条件,获取投票统计的所有信息
	 * 
	 * @return
	 */
	public List<VoteRecordQuery> getVoteRecordByPageItemNameAndDate(
			VoteRecordQuery voteRecordForm);

	/**
	 * 根据相应条件,获取投票统计的所有数量
	 * 
	 * @return
	 */
	public int getVoteRecordCount(VoteRecordQuery voteRecordForm);

	/**
	 * 根据相应条件,获取投票统计的所有信息
	 * 
	 * @return
	 */
	public List<VoteRecordQuery> getVoteRecordUserByPageItemNameAndDate(
			VoteRecordQuery voteRecordForm);

	/**
	 * 根据相应条件,获取投票统计的所有数量
	 * 
	 * @return
	 */
	public int getVoteRecordUserCount(VoteRecordQuery voteRecordForm);

	/**
	 * 获取投票的所有数据信息
	 * 
	 * @return
	 */
	public List<VoteRecord> getVoteRecords();

}