package dao.product;

import java.util.List;

import dto.product.HomePageShowHistory;

public interface IHomePageShowHistoryDao {

	List<String> getAWeekendPageShowHistoriesByType(String type, int siteId);

	void insertOrUpdateHomePageProductShowData(
			HomePageShowHistory homePageShowHistory);

}
