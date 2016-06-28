package services.product;

import java.util.List;

public interface IHomePageProductShowHistoryService {

	void insertOrUpdateHomePageProductShowData();

	List<String> getAWeekendPageShowHistoriesByType(String type, int siteId);

}
