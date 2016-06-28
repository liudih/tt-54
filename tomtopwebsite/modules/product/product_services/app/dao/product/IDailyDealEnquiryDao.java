package dao.product;

import java.util.Date;
import java.util.List;

import dao.IProductEnquiryDao;
import dto.product.DailyDeal;

public interface IDailyDealEnquiryDao extends IProductEnquiryDao {
	public List<DailyDeal> getListingIdByWebsiteId(Integer websiteId,
			Integer pagesize, Integer pageNum);

	public List<DailyDeal> getDailyDealsByWebsiteIdAndDate(Integer websiteId,
			Date startDate, Date endDate);
}
