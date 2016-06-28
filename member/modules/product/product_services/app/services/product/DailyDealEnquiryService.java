package services.product;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import services.search.IDailyDealEnquiryService;
import dao.product.IDailyDealEnquiryDao;
import dto.product.DailyDeal;

public class DailyDealEnquiryService implements IDailyDealEnquiryService {
	@Inject
	IDailyDealEnquiryDao dailyDealEnquiryDao;

	/* (non-Javadoc)
	 * @see services.product.IDailyDealEnquiryService#getListingIdByWebsiteId(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public List<DailyDeal> getListingIdByWebsiteId(Integer websiteId,
			Integer pagesize, Integer pageNum) {
		return dailyDealEnquiryDao.getListingIdByWebsiteId(websiteId, pagesize,
				pageNum);
	};

	/* (non-Javadoc)
	 * @see services.product.IDailyDealEnquiryService#getDailyDealsByWebsiteIdAndDate(java.lang.Integer, java.util.Date, java.util.Date)
	 */
	public List<DailyDeal> getDailyDealsByWebsiteIdAndDate(Integer websiteId,
			Date startDate, Date endDate) {
		return dailyDealEnquiryDao.getDailyDealsByWebsiteIdAndDate(websiteId,
				startDate, endDate);
	}

	/* (non-Javadoc)
	 * @see services.product.IDailyDealEnquiryService#getDailyDealsByNowAfterDay(java.lang.Integer, java.lang.Integer)
	 */
	public List<DailyDeal> getDailyDealsByNowAfterDay(Integer websiteId,
			Integer afterDay) {
		// 计算下个月的dailyDeals
		Calendar startDate = Calendar.getInstance();// 获取当前日期
		startDate.add(Calendar.DAY_OF_MONTH, afterDay);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, afterDay);
		endDate.set(Calendar.HOUR_OF_DAY, 23);
		endDate.set(Calendar.MINUTE, 59);
		endDate.set(Calendar.SECOND, 59);
		endDate.set(Calendar.MILLISECOND, 999);
		return dailyDealEnquiryDao.getDailyDealsByWebsiteIdAndDate(websiteId,
				startDate.getTime(), endDate.getTime());
	}
}
