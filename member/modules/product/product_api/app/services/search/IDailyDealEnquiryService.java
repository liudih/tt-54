package services.search;

import java.util.Date;
import java.util.List;

import dto.product.DailyDeal;

public interface IDailyDealEnquiryService {

	public abstract List<DailyDeal> getListingIdByWebsiteId(Integer websiteId,
			Integer pagesize, Integer pageNum);

	public abstract List<DailyDeal> getDailyDealsByWebsiteIdAndDate(
			Integer websiteId, Date startDate, Date endDate);

	/**
	 * 查找当前时间之后的dailydeal
	 * 
	 * @param websiteId
	 * @param afterDay
	 * @return
	 */
	public abstract List<DailyDeal> getDailyDealsByNowAfterDay(
			Integer websiteId, Integer afterDay);

}