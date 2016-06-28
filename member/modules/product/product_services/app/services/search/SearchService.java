package services.search;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.FluentIterable;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.google.common.collect.Ordering;

import play.Logger;
import play.libs.F;
import services.base.FoundationService;
import services.search.aggs.CategorysSearchAggregation;
import valueobjects.base.Page;
import valueobjects.search.IScriptField;
import valueobjects.search.ISearchCriteria;
import valueobjects.search.ISearchFilter;
import valueobjects.search.ISortOrder;
import valueobjects.search.SearchContext;
import valueobjects.search.SearchPage;
import context.WebContext;

@Singleton
public class SearchService implements ISearchService, IAsyncSearchService {

	@Inject
	Client esclient;

	@Inject
	FoundationService foundation;

	@Inject
	IAsyncSearchService genericSearch;

	@Override
	public Page<String> search(SearchContext context, int siteId, int languageId) {
		SearchRequestBuilder request = prepareSearchRequest(context, siteId);
		SearchResponse response = request.execute().actionGet();
		List<String> list = FluentIterable
				.from(Arrays.asList(response.getHits().getHits()))
				.transform((SearchHit h) -> h.id()).toList();

		Logger.trace("Search Result: {}", list);
		return this.getSearchResult(context, response, list);
	}

	@Override
	public Page<String> searchByContext(SearchContext context,
			WebContext webContext) {
		int siteId = foundation.getSiteID(webContext);
		return this.search(context, siteId, foundation.getLanguage(webContext));
	}

	private Page<String> getSearchResult(SearchContext context,
			SearchResponse response, List<String> hitslist) {
		if (context.getiSearchAggregation() == null) {
			Page<String> result = new Page<String>(hitslist, (int) response
					.getHits().getTotalHits(), context.getPage(),
					context.getPageSize());
			context.setResult(result);
			return result;
		} else {
			Page<String> result = new SearchPage<String>(hitslist,
					(int) response.getHits().getTotalHits(), context.getPage(),
					context.getPageSize(), context.getiSearchAggregation()
							.getAggValue(response));
			context.setResult(result);
			return result;
		}
	}

	public <T> Page<T> searchByP(SearchContext context, int siteId,
			int languageId, SearchResultExtractor<T> extractor) {
		SearchRequestBuilder request = prepareSearchRequest(context, siteId);
		SearchResponse response = request.execute().actionGet();
		List<T> list = FluentIterable
				.from(Arrays.asList(response.getHits().getHits()))
				.transform((SearchHit h) -> extractor.extract(h)).toList();

		Logger.trace("Search Result: {}", list);
		return this.getSearchResultT(context, response, list);
	}

	@Override
	public <T> Page<T> searchPByContext(SearchContext context,
			WebContext webContext, SearchResultExtractor<T> extractor) {
		int siteId = foundation.getSiteID(webContext);
		int languageid = foundation.getLanguage(webContext);
		return this.searchByP(context, siteId, languageid, extractor);
	}

	private <T> Page<T> getSearchResultT(SearchContext context,
			SearchResponse response, List<T> hitslist) {
		if (context.getiSearchAggregation() == null) {
			Page<T> result = new Page<T>(hitslist, (int) response.getHits()
					.getTotalHits(), context.getPage(), context.getPageSize());
			return result;
		} else {
			Page<T> result = new SearchPage<T>(hitslist, (int) response
					.getHits().getTotalHits(), context.getPage(),
					context.getPageSize(), context.getiSearchAggregation()
							.getAggValue(response));
			return result;
		}
	}

	protected SearchRequestBuilder prepareSearchRequest(SearchContext context,
			int siteId) {
		BoolQueryBuilder q = QueryBuilders.boolQuery();
		if (context.getCriteria().isEmpty()) {
			q.must(QueryBuilders.matchAllQuery());
		} else {
			for (ISearchCriteria criteria : context.getCriteria()) {
				q.must(criteria.getQueryBuilder());
			}
		}

		SearchRequestBuilder request = esclient.prepareSearch("product")
				.setTypes(Integer.toString(siteId))
				.setFrom(context.getPage() * context.getPageSize())
				.setSize(context.getPageSize()).setQuery(q);

		for (IScriptField f : context.getScriptField()) {
			request.addScriptField(f.getName(), f.getScript());
		}

		if (context.getFilter().size() > 0) {
			BoolFilterBuilder bool = FilterBuilders.boolFilter();
			for (ISearchFilter filter : context.getFilter()) {
				bool.must(filter.getFilter());
			}
			request.setPostFilter(bool);
		}

		for (ISortOrder order : Ordering.natural()
				.onResultOf((ISortOrder s) -> s.sortOrder())
				.sortedCopy(context.getSort())) {
			request.addSort(order.getSortBuilder());
		}

		if (context.getiSearchAggregation() != null) {
			request.addAggregation(context.getiSearchAggregation()
					.getAggBuilder());
		}

		return request;
	}

	@Override
	public Page<String> getProductByKeyWord(SearchContext context,
			WebContext webContext) {
		Page<String> listingsProm = genericSearch.getasynSearch(context,
				webContext);
		return listingsProm;
	}

	@Override
	public F.Promise<Page<String>> asyncSearch(SearchContext context,
			int siteId, int languageId) {
		SearchRequestBuilder request = prepareSearchRequest(context, siteId);
		SearchActionListener listener = new SearchActionListener();
		return listener.execute(request).map(
				response -> {
					List<SearchHit> hits = Arrays.asList(response.getHits()
							.getHits());
					List<String> list = FluentIterable.from(hits)
							.transform((SearchHit h) -> {
								return h.id();
							}).toList();
					return this.getSearchResult(context, response, list);
				});
	}

	@Override
	public Page<String> getasynSearch(SearchContext context,
			WebContext webContext) {
		try {
			int siteId = foundation.getSiteID(webContext);
			int languageId = foundation.getLanguage(webContext);
			F.Promise<Page<String>> p = this.asyncSearch(context, siteId,
					languageId);
			Page<String> listItem = p.get(60000);
			return listItem;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
