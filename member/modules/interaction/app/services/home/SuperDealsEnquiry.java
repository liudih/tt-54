package services.home;

import interceptors.CacheResult;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import services.base.utils.DateFormatUtils;
import services.search.ISearchService;
import services.search.SearchContextFactory;
import services.search.criteria.CategorySearchCriteria;
import services.search.criteria.DisCountCriteria;
import services.search.criteria.DiscountOnlySearchCriteria;
import services.search.filter.DiscountDateFilter;
import services.search.filter.PriceRangeFilter;
import services.search.sort.DiscountSortOrder;
import valueobjects.base.Page;

import com.google.common.collect.Range;

import valueobjects.search.SearchContext;
import extensions.interaction.search.sort.ReviewCountSortOrder;

public class SuperDealsEnquiry {
	@Inject
	SearchContextFactory contextFactory;

	@Inject
	ISearchService searchService;

	/**
	 * @param page
	 * @param perPage
	 * @return Page<String> 返回listingIds
	 */
	@CacheResult("product.customed_search_result")
	public Page<String> getSuperDealsListingIdPage(int siteId, int language,
			int page, int perPage) {
		List<Date> dlist = DateFormatUtils.getNowDayRange(0);
		List<Date> dlist2 = DateFormatUtils.getNowDayRange(90); // 设置时间区间为90天
		SearchContext context = contextFactory
				.pureSearch(new DiscountOnlySearchCriteria());
		context.getSort().add(new DiscountSortOrder(false)); // 根据折扣数最大降序排列，也就是对客户最实惠
		context.getSort().add(new ReviewCountSortOrder(false)); // 根据评论数降序排
		context.getFilter().add(
				new DiscountDateFilter(
						Range.closed(dlist.get(0), dlist2.get(0))));
		context.setPage(page);
		context.setPageSize(perPage);
		return searchService.search(context, siteId, language);
	}

	// 第二次访问用户
	public Page<String> getSuperDealsListingIdPage(Integer siteId,
			Integer language, int page, Integer perPage, Integer rootCategoryId) {
		SearchContext context = contextFactory
				.pureSearch(new DiscountOnlySearchCriteria());
		context.getCriteria().add(new CategorySearchCriteria(rootCategoryId));
		context.getSort().add(new DiscountSortOrder(false)); // 根据折扣数最大降序排列，也就是对客户最实惠
		context.getSort().add(new ReviewCountSortOrder(false)); // 根据评论数降序排
		context.setPage(page);
		context.setPageSize(perPage);
		return searchService.search(context, siteId, language);
	}

	// superDeal新的搜索条件
	public List<String> getAllQualifiedListingIds(Integer siteId,
			Integer language, List<Double> priceRange,
			List<Double> discountRange) {
		SearchContext context = contextFactory
				.pureSearch(new DiscountOnlySearchCriteria());
		context.getCriteria().add(
				new DisCountCriteria(Range.closed(discountRange.get(0),
						discountRange.get(1))));
		context.getFilter().add(
				new PriceRangeFilter(Range.closed(priceRange.get(0),
						priceRange.get(1))));
		context.getSort().add(new DiscountSortOrder(false)); // 根据折扣数最大降序排列，也就是对客户最实惠
		context.getSort().add(new ReviewCountSortOrder(false)); // 根据评论数降序排
		Page<String> listingPage = searchService.search(context, siteId,
				language);
		return listingPage.getList();
	}
}
