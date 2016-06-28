package dao.product.impl;

import mapper.product.DailyDealMapper;

import com.google.inject.Inject;

import dao.product.IDailyDealUpdateDao;
import dto.product.DailyDeal;

public class DailyDealUpdateDao implements IDailyDealUpdateDao {
	@Inject
	DailyDealMapper dailyDealMapper;

	@Override
	public int insert(Integer iwebsiteid, String clistingid, String csku,
			String ccreateuse, boolean bvalid) {
		return dailyDealMapper.insert(iwebsiteid, clistingid, csku, ccreateuse,
				bvalid);
	}

	@Override
	public int updateDailyDealBvalid(Integer id, Boolean bvalid) {
		return dailyDealMapper.updateDailyDealBvalid(id, bvalid);
	}

	@Override
	public int insert(DailyDeal dailyDeal) {
		return dailyDealMapper.insertDailyDeal(dailyDeal);
	}
}
