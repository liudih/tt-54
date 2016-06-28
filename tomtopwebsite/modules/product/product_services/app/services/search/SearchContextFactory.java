package services.search;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import play.mvc.Http.Context;
import services.common.UUIDGenerator;
import services.search.aggs.CategorysSearchAggregation;
import services.search.criteria.StatusSearch;
import valueobjects.search.ISearchAggregation;
import valueobjects.search.ISearchCriteria;
import valueobjects.search.ISearchFilter;
import valueobjects.search.ISearchQueryParser;
import valueobjects.search.ISearchUIProvider;
import valueobjects.search.ISortOrder;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

import valueobjects.search.SearchContext;

public class SearchContextFactory implements ISearchContextFactory {

	@Inject
	Set<ISearchQueryParser> parsers;

	@Inject
	Set<ISearchUIProvider> uiProviders;

	public SearchContext fromCurrentHttpContext(ISearchCriteria mainCriteria) {
		return fromHttpContext(mainCriteria, Context.current());
	}

	public SearchContext fromHttpContext(ISearchCriteria mainCriteria,
			Context ctx) {
		return fromQueryHttpContext(mainCriteria, ctx.request().queryString(),
				ctx, null);
	}

	public SearchContext pureSearch(ISearchCriteria mainCriteria) {
		return fromQueryHttpContext(mainCriteria, Maps.newHashMap(), null, null);
	}

	public SearchContext fromQueryCurrentHttpContext(
			ISearchCriteria mainCriteria, Map<String, String[]> queryStrings) {
		return fromQueryHttpContext(mainCriteria, queryStrings,
				Context.current(), null);
	}

	public SearchContext fromQueryHttpContext(ISearchCriteria mainCriteria,
			Map<String, String[]> queryStrings, Context ctx,
			ISearchAggregation iSearchAggregation) {
		SearchContext sc = newSearch(mainCriteria, null, null, null, ctx);
		sc.setiSearchAggregation(new CategorysSearchAggregation());
		for (ISearchQueryParser p : parsers) {
			p.parse(queryStrings, sc);
		}
		return sc;
	}

	@Override
	public SearchContext fromQueryString(ISearchCriteria mainCriteria,
			Map<String, String[]> queryStrings, Set<String> ulfilters) {
		SearchContext sc = newSearch(mainCriteria, null, null, ulfilters,
				Context.current());
		sc.setiSearchAggregation(new CategorysSearchAggregation());
		for (ISearchQueryParser p : parsers) {
			p.parse(queryStrings, sc);
		}
		return sc;
	}

	/**
	 * 内部使用，不指定status状态
	 * 
	 * @return
	 */
	public SearchContext personalfromQueryString(ISearchCriteria mainCriteria,
			Map<String, String[]> queryStrings) {
		SearchContext sc = personalSearch(mainCriteria, null, null, null);
		for (ISearchQueryParser p : parsers) {
			p.parse(queryStrings, sc);
		}
		return sc;
	}

	public SearchContext personalfromQueryString(ISearchCriteria mainCriteria) {
		return personalfromQueryString(mainCriteria, Context.current()
				.request().queryString());
	}

	/**
	 * 创建和初始化搜索上下文
	 * 
	 * @param initCriteria
	 * @param initOrder
	 * @param initFilter
	 * @param ctx
	 *            HTTP Context
	 * @return
	 */
	public SearchContext newSearch(ISearchCriteria initCriteria,
			ISortOrder initOrder, ISearchFilter initFilter,
			final Set<String> ulfilters, Context ctx) {
		// 过滤ui
		if (ulfilters != null && ulfilters.size() > 0) {
			ulfilters.add("popularity");
			uiProviders = Sets.filter(uiProviders, ui -> {
				return !ulfilters.contains(ui.getName());
			});
		}
		SearchContext sc = new SearchContext();
		sc.setUIProviders(FluentIterable.from(uiProviders).toSortedList(
				Ordering.natural().onResultOf(
						(ISearchUIProvider ui) -> ui.getDisplayOrder())));
		if (initCriteria != null) {
			sc.getCriteria().add(initCriteria);
		}
		if (initOrder != null) {
			sc.getSort().add(initOrder);
		}
		if (initFilter != null) {
			sc.getFilter().add(initFilter);
		}
		StatusSearch filterStatus = new StatusSearch();
		sc.getCriteria().add(filterStatus);
		sc.setId(UUIDGenerator.createAsString());
		return sc;
	}

	/**
	 * 内部使用，不指定status状态
	 * 
	 * @return
	 */
	public SearchContext personalSearch(ISearchCriteria initCriteria,
			ISortOrder initOrder, ISearchFilter initFilter,
			final Set<String> ulfilters) {
		// 过滤ui
		if (ulfilters != null && ulfilters.size() > 0) {
			uiProviders = Sets.filter(uiProviders, ui -> {
				return !ulfilters.contains(ui.getName());
			});
		}
		SearchContext sc = new SearchContext();
		sc.setUIProviders(FluentIterable.from(uiProviders).toSortedList(
				Ordering.natural().onResultOf(
						(ISearchUIProvider ui) -> ui.getDisplayOrder())));
		if (initCriteria != null) {
			sc.getCriteria().add(initCriteria);
		}
		if (initOrder != null) {
			sc.getSort().add(initOrder);
		}
		if (initFilter != null) {
			sc.getFilter().add(initFilter);
		}
		sc.setId(UUIDGenerator.createAsString());
		Context.current().args.put("searchContext", sc);
		return sc;
	}

	/**
	 * 返回上次搜索上下文
	 * 
	 * @return
	 */
	public SearchContext lastSearchContext() {
		return new SearchContext();
	}
}
