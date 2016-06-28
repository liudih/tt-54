package services.home;

import interceptors.CacheResult;

import java.util.List;

import play.Logger;
import services.base.FoundationService;
import services.product.IHomePageProductShowHistoryService;

import com.google.inject.Inject;

import dao.product.IHomePageShowHistoryDao;
import dto.product.HomePageShowHistory;
import dto.product.HomePageShowHistoryTypeEnum;

public class HomePageProductShowHistoryService implements
		IHomePageProductShowHistoryService {

	@Inject
	IHomePageShowHistoryDao homePageShowHistoryDao;

	@Inject
	HomePageDataEnquiry homePageDataEnquiry;

	@Inject
	FoundationService foundationService;

	@Override
	public void insertOrUpdateHomePageProductShowData() {
		int languageId = foundationService.getDefaultLanguage();
		int siteId = 1;
		int page = 0; // 从第一页开始

		Logger.debug("------insert or update home page product begin-----------");
		List<String> hotProductPageListingids = homePageDataEnquiry
				.getHotSalesListingIds(siteId, languageId, page);
		this.insertOrUpdateBatch(hotProductPageListingids, siteId,
				HomePageShowHistoryTypeEnum.HOT.getType());
		List<String> clearProductPage = homePageDataEnquiry
				.getClearanceSalesListingIds(siteId, languageId, page);
		this.insertOrUpdateBatch(clearProductPage, siteId,
				HomePageShowHistoryTypeEnum.CLEARSTOCKS.getType());
		List<String> featureProductPage = homePageDataEnquiry
				.getFeaturedItemsListingIds(siteId, languageId, page);
		this.insertOrUpdateBatch(featureProductPage, siteId,
				HomePageShowHistoryTypeEnum.FEATURED.getType());
		// 增加newArrivals产品展示，给mobile调用
		List<String> newArrivalsProductPage = homePageDataEnquiry
				.getNewArrivalsListingIds(siteId, languageId, page);
		this.insertOrUpdateBatch(newArrivalsProductPage, siteId,
				HomePageShowHistoryTypeEnum.NEWARRIVALS.getType());
		// 增加freeshipping产品展示，给mobile调用
		List<String> freeshippingProductPage = homePageDataEnquiry
				.getFreeshippingListingIds(siteId, languageId, page);
		this.insertOrUpdateBatch(freeshippingProductPage, siteId,
				HomePageShowHistoryTypeEnum.FREESHIPPING.getType());
		Logger.debug("------insert or update home page product over-----------");
	}

	@Override
	@CacheResult("product.customed_search_result")
	public List<String> getAWeekendPageShowHistoriesByType(String type,
			int siteId) {
		return homePageShowHistoryDao.getAWeekendPageShowHistoriesByType(type,
				siteId);
	}

	public void insertOrUpdateBatch(List<String> listingIds, Integer siteId,
			String type) {
		if (null != listingIds && listingIds.size() > 0) {
			for (String listingid : listingIds) {
				HomePageShowHistory homePageShowHistory = new HomePageShowHistory();
				homePageShowHistory.setClistingid(listingid);
				homePageShowHistory.setCtype(type);
				homePageShowHistory.setIwebsiteid(siteId);
				homePageShowHistoryDao
						.insertOrUpdateHomePageProductShowData(homePageShowHistory);
			}
		}
	}
}
