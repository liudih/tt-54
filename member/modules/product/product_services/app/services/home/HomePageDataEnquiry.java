package services.home;

import interceptors.CacheResult;

import java.util.List;

import play.Logger;
import services.base.FoundationService;
import services.product.IHomePageDataEnquiry;
import services.product.IHomePageProductShowHistoryService;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import services.search.criteria.CategorySearchCriteria;
import services.search.criteria.ProductLabelType;
import services.search.criteria.ProductTagsCriteria;
import services.search.sort.DiscountSortOrder;
import services.search.sort.TagDateSortOrder;
import valueobjects.base.Page;
import valueobjects.search.SearchContext;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import dao.product.IProductLabelEnquiryDao;
import dao.product.impl.ProductLabelEnquiryDao;
import dto.product.HomePageShowHistoryTypeEnum;

public class HomePageDataEnquiry implements IHomePageDataEnquiry {

	@Inject
	SearchContextFactory contextFactory;

	@Inject
	ISearchService searchService;

	@Inject
	FoundationService foundationService;

	@Inject
	IHomePageProductShowHistoryService homePageProductShowHistoryService;

	@Inject
	IProductLabelEnquiryDao productLabelEnquiryDao;

	// new arrivals
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * services.home.IHomePageDataEnquiry#getNewArrialListingIdsByCategoryId
	 * (java.lang.Integer, int, int)
	 */
	@CacheResult("product.customed_search_result")
	public Page<String> getNewArrialListingIdsByCategoryId(Integer categoryId,
			int siteId, int languageId) {
		SearchContext context = contextFactory
				.pureSearch(new CategorySearchCriteria(categoryId));
		context.getCriteria().add(
				new ProductTagsCriteria(ProductLabelType.NewArrial.toString()));
		context.setPage(0);
		context.setPageSize(5);
		return searchService.search(context, siteId, languageId);
	}

	@CacheResult("product.customed_search_result")
	public Page<String> getHotSalesPage(int siteId, int languageId, int page) {
		int pageSize = 10;
		SearchContext context = contextFactory
				.pureSearch(new ProductTagsCriteria(ProductLabelType.Hot
						.toString()));
		context.getSort().add(new DiscountSortOrder(false));
		context.getSort().add(
				new TagDateSortOrder(ProductLabelType.Hot.toString()));
		context.setPage(page);
		context.setPageSize(pageSize);
		return searchService.search(context, siteId, languageId);
	}

	@Override
	@CacheResult("home")
	public List<String> getHotSalesListingIds(int siteId, int languageId,
			int page) {
		Page<String> hotListingPage = productLabelEnquiryDao
				.getProductLabelPageByTypeAndWebsite(siteId,
						HomePageShowHistoryTypeEnum.HOT.getType(), page, 10);
		return hotListingPage.getList();

	}

	// clearance sales
	/*
	 * (non-Javadoc)
	 * 
	 * @see services.home.IHomePageDataEnquiry#getClearanceSalesListingIds(int,
	 * int)
	 */
	@CacheResult("product.customed_search_result")
	public Page<String> getClearanceSalesListingIdsPage(int siteId,
			int languageId, int page) {
		SearchContext context = contextFactory
				.pureSearch(new ProductTagsCriteria(
						ProductLabelType.Clearstocks.toString()));
		context.getSort().add(new DiscountSortOrder(false));
		context.getSort().add(
				new TagDateSortOrder(ProductLabelType.Clearstocks.toString()));
		context.setPage(page);
		context.setPageSize(10);
		return searchService.search(context, siteId, languageId);
	}

	@Override
	@CacheResult("home")
	public List<String> getClearanceSalesListingIds(int siteId, int languageId,
			int page) {
		Page<String> clearListingPage = productLabelEnquiryDao
				.getProductLabelPageByTypeAndWebsite(siteId,
						HomePageShowHistoryTypeEnum.CLEARSTOCKS.getType(),
						page, 10);
		return clearListingPage.getList();
	}

	// feature Items
	/*
	 * (non-Javadoc)
	 * 
	 * @see services.home.IHomePageDataEnquiry#getFeaturedItemsListingIds(int,
	 * int)
	 */
	@CacheResult("product.customed_search_result")
	public Page<String> getFeaturedItemsListingIdsPage(int siteId,
			int languageId, int page) {
		SearchContext context = contextFactory

		.pureSearch(new ProductTagsCriteria(ProductLabelType.Featured
				.toString()));
		context.getSort().add(new DiscountSortOrder(false));
		context.getSort().add(
				new TagDateSortOrder(ProductLabelType.Featured.toString()));
		context.setPage(page);
		context.setPageSize(10);
		return searchService.search(context, siteId, languageId);
	}

	@Override
	@CacheResult("home")
	public List<String> getFeaturedItemsListingIds(int siteId, int languageId,
			int page) {
		int pageSize = 10;
		Page<String> featuredListingPage = productLabelEnquiryDao
				.getProductLabelPageByTypeAndWebsite(siteId,
						HomePageShowHistoryTypeEnum.FEATURED.getType(), page,
						pageSize);
		return featuredListingPage.getList();
	}

	@Override
	@CacheResult("home")
	public List<String> getNewArrivalsListingIds(int siteId, int languageId,
			int page) {
		Page<String> newArrivalsListingPage = this.getNewArrivalsPage(siteId,
				languageId, page);
		return newArrivalsListingPage.getList();

	}

	/**
	 * 给mobile单独调用展示new Arrivals
	 * 
	 * @author xiaoch
	 * @param siteId
	 * @param languageId
	 * @param page
	 * @return 12条listingid
	 */
	private Page<String> getNewArrivalsPage(int siteId, int languageId, int page) {
		int pageSize = 12;
		SearchContext context = contextFactory
				.pureSearch(new ProductTagsCriteria(ProductLabelType.NewArrial
						.toString()));
		context.getSort().add(new DiscountSortOrder(false));
		context.getSort().add(
				new TagDateSortOrder(ProductLabelType.NewArrial.toString()));
		context.setPage(page);
		context.setPageSize(pageSize);
		return searchService.search(context, siteId, languageId);
	}

	@Override
	public List<String> getFreeshippingListingIds(int siteId, int languageId,
			int page) {
		List<String> freeshippingHistoryListingIds = homePageProductShowHistoryService
				.getAWeekendPageShowHistoriesByType(
						HomePageShowHistoryTypeEnum.FREESHIPPING.getType(),
						siteId);
		Page<String> freeshippingListingPage = null;
		List<String> listingIds = Lists.newArrayList();
		while (listingIds.size() < 2) {
			freeshippingListingPage = this.getFreeshippingPage(siteId,
					languageId, page);
			if (null != freeshippingListingPage) {
				listingIds.addAll(freeshippingListingPage.getList());
			}
			if (null != freeshippingHistoryListingIds
					&& freeshippingHistoryListingIds.size() > 0) {
				listingIds.removeAll(freeshippingHistoryListingIds);
			}
			if (page == 20) {
				break;
			}
			page = page + 1;
		}
		if (listingIds.size() > 2) {
			listingIds = listingIds.subList(0, 2);
		}
		return listingIds;
	}

	/**
	 * 给mobile单独调用展示freeshipping
	 * 
	 * @author xiaoch
	 * @param siteId
	 * @param languageId
	 * @param page
	 * @return 2条listingid
	 */
	private Page<String> getFreeshippingPage(int siteId, int languageId,
			int page) {
		int pageSize = 2;
		SearchContext context = contextFactory
				.pureSearch(new ProductTagsCriteria(
						ProductLabelType.FreeShipping.toString()));
		context.getSort().add(new DiscountSortOrder(false));
		context.getSort().add(
				new TagDateSortOrder(ProductLabelType.FreeShipping.toString()));
		context.setPage(page);
		context.setPageSize(pageSize);
		return searchService.search(context, siteId, languageId);
	}

}
