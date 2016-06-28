package dao.product.impl;

import java.util.List;

import mapper.product.HomePageShowHistoryMapper;

import com.google.inject.Inject;

import dao.product.IHomePageShowHistoryDao;
import dto.product.HomePageShowHistory;

public class HomePageShowHistoryDao implements IHomePageShowHistoryDao {

	@Inject
	HomePageShowHistoryMapper homePageShowHistoryMapper;

	@Override
	public List<String> getAWeekendPageShowHistoriesByType(String type,
			int siteId) {
		return homePageShowHistoryMapper.getAWeekendPageShowHistoriesByType(
				type, siteId);
	}

	@Override
	public void insertOrUpdateHomePageProductShowData(
			HomePageShowHistory homePageShowHistory) {
		String listingId = homePageShowHistory.getClistingid();
		String type = homePageShowHistory.getCtype();
		Integer siteId = homePageShowHistory.getIwebsiteid();
		HomePageShowHistory hoHistory = homePageShowHistoryMapper
				.getHomePageProductHistory(listingId, siteId, type);
		if (null != hoHistory) {
			homePageShowHistoryMapper.updateHomePageProductHistory(hoHistory);
		} else {
			homePageShowHistoryMapper
					.addHomePageProductHistory(homePageShowHistory);
		}
	}

}
