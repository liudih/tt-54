package dao.product.impl;

import java.util.Date;
import java.util.List;

import mapper.product.DailyDealMapper;

import com.google.inject.Inject;

import dao.product.IDailyDealEnquiryDao;
import dto.product.DailyDeal;

public class DailyDealEnquiryDao implements IDailyDealEnquiryDao {
	@Inject
	DailyDealMapper dailyDealMapper;

	@Override
	public List<DailyDeal> getListingIdByWebsiteId(Integer websiteId,
			Integer pagesize, Integer pageNum) {
		return dailyDealMapper.getListingIdByWebsiteId(websiteId, pagesize,
				pageNum);
	}

	@Override
	public List<DailyDeal> getDailyDealsByWebsiteIdAndDate(Integer websiteId,
			Date startDate, Date endDate) {
		return dailyDealMapper.getListingIdByWebsiteIdAndDate(websiteId,
				startDate, endDate);
	}

}
