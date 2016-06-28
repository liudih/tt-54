package dao.activitydb.page.impl;

import java.util.Date;
import java.util.List;

import values.activity.page.PageItemCount;
import values.activity.page.VoteRecordQuery;
import mapper.activitydb.page.VoteRecordMapper;

import com.google.inject.Inject;

import dao.activitydb.page.IVoteRecordDao;
import entity.activity.page.VoteRecord;

public class VoteRecordDaoImpl implements IVoteRecordDao {

	@Inject
	VoteRecordMapper voteRecordMapper;

	@Override
	public int deleteByPrimaryKey(Integer iid) {
		return voteRecordMapper.deleteByPrimaryKey(iid);
	}

	@Override
	public int insert(VoteRecord record) {
		return voteRecordMapper.insert(record);
	}

	@Override
	public VoteRecord selectByPrimaryKey(Integer iid) {
		return voteRecordMapper.selectByPrimaryKey(iid);
	}

	@Override
	public int updateByPrimaryKey(VoteRecord record) {
		return voteRecordMapper.updateByPrimaryKey(record);
	}
	
	@Override
	public int getPageItemCount(int itemid,int websiteid){
		return voteRecordMapper.getPageItemCount(itemid, websiteid);
	}

	@Override
	public List<PageItemCount> getPageAllItemCount(int pageId, int websiteid) {
		return voteRecordMapper.getPageAllItemCount(pageId, websiteid);
	}

	@Override
	public int getUserPageItemCount(String email, int pageid, int websiteid) {
		return voteRecordMapper.getUserPageItemCount(email, pageid, websiteid);
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: getPageItemCountToday</p>
	 * <p>Description: 查询投票记录通过站点、用户名、投票项、当前日期、明天日期</p>
	 * @param websiteId
	 * @param memberID
	 * @param activityPageItemId
	 * @param date
	 * @param tomorrowDate
	 * @return
	 * @see dao.activitydb.page.IVoteRecordDao#getPageItemCountToday(int, java.lang.String, int, java.util.Date, java.util.Date)
	 */
	@Override
	public int getPageItemCountToday(int websiteId, String memberID,
			int activityPageItemId, Date date, Date tomorrowDate) {
		return voteRecordMapper.getPageItemCountToday(websiteId, memberID,
				activityPageItemId, date, tomorrowDate);
	}
	
	
	@Override
	public List<VoteRecordQuery> getVoteRecordByPageItemNameAndDate(
			VoteRecordQuery VoteRecordQuery) {
		return voteRecordMapper.getVoteRecordByPageItemNameAndDate(
				VoteRecordQuery.getIpageitemid(), VoteRecordQuery.getStartDate(),
				VoteRecordQuery.getEndDate(), VoteRecordQuery.getPageSize(),
				VoteRecordQuery.getPageNum());
	}

	@Override
	public int getVoteRecordCount(VoteRecordQuery VoteRecordQuery) {
		return voteRecordMapper.getCount(VoteRecordQuery.getIpageitemid(),
				VoteRecordQuery.getStartDate(), VoteRecordQuery.getEndDate());
	}

	@Override
	public List<VoteRecordQuery> getVoteRecordUserByPageItemNameAndDate(
			VoteRecordQuery VoteRecordQuery) {
		return voteRecordMapper.getVoteRecordUserByPageItemNameAndDate(
				VoteRecordQuery.getIpageitemid(), VoteRecordQuery.getStartDate(),
				VoteRecordQuery.getEndDate(), VoteRecordQuery.getPageSize(),
				VoteRecordQuery.getPageNum());
	}

	@Override
	public int getVoteRecordUserCount(VoteRecordQuery VoteRecordQuery) {
		return voteRecordMapper.getUserCount(VoteRecordQuery.getIpageitemid(),
				VoteRecordQuery.getStartDate(), VoteRecordQuery.getEndDate());
	}

	@Override
	public List<VoteRecord> getVoteRecords() {
		return voteRecordMapper.selectAll();
	}

}
