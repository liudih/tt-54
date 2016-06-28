package service.activity.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import play.Logger;

import com.google.inject.Inject;

import dao.activitydb.page.IVoteRecordDao;
import service.activity.IVoteRecordService;
import values.activity.page.PageItemCount;

public class VoteRecordService implements IVoteRecordService {

	@Inject
	IVoteRecordDao iVoteRecordDao;
	
	//@CacheResult(expiration=300)
	public int getPageItemCount(int itemid, int websiteid){
		return iVoteRecordDao.getPageItemCount(itemid, websiteid);
	}
	//@CacheResult(expiration=30)
	public List<PageItemCount> getPageAllItemCount(int pageId, int websiteid){
		return iVoteRecordDao.getPageAllItemCount(pageId, websiteid);
	}

	@Override
	public int getUserPageItemCount(String email, int pageid, int websiteid) {
		// TODO Auto-generated method stub
		return iVoteRecordDao.getUserPageItemCount(email, pageid, websiteid);
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
	 * @see service.vote.IVoteRecordService#getPageItemCountToday(int, java.lang.String, int, java.util.Date, java.util.Date)
	 */
	@Override
	public int getPageItemCountToday(int websiteId, String memberID,
			int activityPageItemId, Date date, Date tomorrowDate) {
		date = format(date);
		tomorrowDate = format(tomorrowDate);
		return iVoteRecordDao.getPageItemCountToday(websiteId, memberID,
				activityPageItemId, date, tomorrowDate);
	}
	
	/**
	 * 
	 * @Title: format
	 * @Description: TODO(去掉时间的时分秒)
	 * @param @param date
	 * @param @return
	 * @return Date
	 * @throws 
	 * @author yinfei
	 */
	private Date format(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			Logger.error("date format error : " + e.getMessage());
		}
		return date;
	}
}
