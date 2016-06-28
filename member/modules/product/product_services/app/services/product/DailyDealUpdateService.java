package services.product;

import javax.inject.Inject;

import dao.product.IDailyDealUpdateDao;
import dto.product.DailyDeal;

public class DailyDealUpdateService {
	@Inject
	IDailyDealUpdateDao dailyDealUpdateDao;

	public boolean insert(Integer iwebsiteid, String clistingid, String csku,
			String ccreateuse, boolean bvalid) {
		return dailyDealUpdateDao.insert(iwebsiteid, clistingid, csku,
				ccreateuse, bvalid) > 0;
	}

	public boolean updateDailyDealBvalid(Integer id, Boolean bvalid) {
		return dailyDealUpdateDao.updateDailyDealBvalid(id, bvalid) > 0;
	}

	public boolean insert(DailyDeal dailyDeal) {
		return dailyDealUpdateDao.insert(dailyDeal) > 0;
	}
}
