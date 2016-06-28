package services.search;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.elasticsearch.common.collect.Collections2;

import play.Logger;
import play.libs.Json;
import services.base.CurrencyService;
import services.base.FoundationService;
import services.base.utils.StringUtils;
import services.base.SystemParameterService;
import services.search.criteria.AttributeSearchCriteria;
import services.search.criteria.PublishDateCriteria;
import services.search.filter.CategorySearchFilter;
import services.search.filter.PriceRangeFilter;
import services.search.filter.ProductTagsFilter;
import services.search.filter.StorageFilter;
import services.search.sort.DateSortOrder;
import services.search.sort.DiscountSortOrder;
import services.search.sort.PriceSortOrder;
import services.search.sort.SaleCountSortOrder;
import services.search.sort.ViewCountSortOrder;
import valueobjects.search.ISearchQueryParser;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import valueobjects.search.SearchContext;

public class ProductSearchQueryParser implements ISearchQueryParser {

	@Inject
	SystemParameterService parameterService;

	@Inject
	FoundationService foundationService;

	@Inject
	CurrencyService currencyService;

	@Override
	public void parse(Map<String, String[]> queryStrings, SearchContext context) {

		if (queryStrings.containsKey("popularity")) {
			context.getSort().add(
					new ViewCountSortOrder(isAsc(queryStrings, "popularity")));
		}
		if (queryStrings.containsKey("publish")) {
			context.getSort().add(
					new DateSortOrder(isAsc(queryStrings, "publish")));
		}
		if (queryStrings.containsKey("sale")) {
			boolean asc = false;
			String[] values = queryStrings.get("sale");
			if (values != null && values.length > 0) {
				asc = ("asc".equalsIgnoreCase(values[0]));
			}
			context.getSort().add(new SaleCountSortOrder(asc));
		}
		if (queryStrings.containsKey("discount")) {
			context.getSort().add(
					new DiscountSortOrder(isAsc(queryStrings, "discount")));
		}
		if (queryStrings.containsKey("publishDate")) {
			context.getCriteria().add(
					new PublishDateCriteria(getDates(queryStrings
							.get("publishDate"))));
		}
		if (queryStrings.containsKey("price")) {
			context.getSort().add(
					new PriceSortOrder(isAsc(queryStrings, "price")));
		}
		if (queryStrings.containsKey("attris")) {
			List<String> attrs = Lists.newArrayList(queryStrings.get("attris"));
			Logger.debug("Attribute Query: {}", attrs);
			context.getCriteria().add(new AttributeSearchCriteria(attrs));
		}
		if (queryStrings.containsKey("p")) {
			int page = getInteger(queryStrings, "p");
			// p is 1-based, page is 0-based
			int pageIdx = page > 0 ? (page - 1) : 0;
			Logger.debug("Page input: {} idx={}", page, pageIdx);
			context.setPage(pageIdx);
		}
		if (queryStrings.containsKey("limit")) {
			int pagesize = getInteger(queryStrings, "limit");
			if (pagesize == 1)
				pagesize = 20;
			context.setPageSize(pagesize);
		}
		if (queryStrings.containsKey("pricerange")) {
			String[] range = queryStrings.get("pricerange");
			String[] rangeSplit = range.length > 0 ? range[0].split(":") : null;
			if (rangeSplit != null && rangeSplit.length == 2) {
				List<String> r = Lists.newArrayList(rangeSplit);
				List<Double> dr = Lists.transform(r, x -> StringUtils
						.isEmpty(x) ? null : Double.parseDouble(x));
				String currency = foundationService.getCurrency();
				Double lowPrice = 0.0;
				Double highPrice = 0.0;
				if (dr.get(0) == null && dr.get(1) != null) {
					highPrice = currencyService.exchange(dr.get(1), currency,
							"USD");
					context.getFilter().add(
							new PriceRangeFilter(Range.atMost(highPrice)));
				} else if (dr.get(1) == null && dr.get(0) != null) {
					lowPrice = currencyService.exchange(dr.get(0), currency,
							"USD");
					context.getFilter().add(
							new PriceRangeFilter(Range.atLeast(lowPrice)));
				} else {
					lowPrice = currencyService.exchange(dr.get(0), currency,
							"USD");
					highPrice = currencyService.exchange(dr.get(1), currency,
							"USD");
					context.getFilter().add(
							new PriceRangeFilter(Range.closed(lowPrice,
									highPrice)));
				}

				Logger.debug("pricerange:{} - {}", dr.get(0), dr.get(1));
			} else {
				throw new RuntimeException(
						"Range has two parts separated by colon: e.g. 1:2");
			}
		}

		// ~ category必须是int类型
		if (queryStrings.containsKey("category")) {
			List<String> category = Lists.newArrayList(queryStrings
					.get("category"));

			if (category != null && category.size() > 0) {
				Pattern pattern = Pattern.compile("[0-9]*");
				Collection<String> nintlist = Collections2.filter(category,
						p -> {
							return p.matches("[0-9]+") == false;
						});
				if (null != nintlist && nintlist.size() > 0) {
					category.removeAll(nintlist);
					Logger.error(
							"-- search category parameter must be int: {}",
							Json.toJson(nintlist));
				}
				// Logger.debug("Category Filter: {}", category);
				if (category.size() > 0 && !category.contains(null)
						&& !category.contains("")) {
					context.getFilter().add(new CategorySearchFilter(category));
				}
			}
		}
		if (queryStrings.containsKey("productType")) {
			List<String> tags = Lists.newArrayList(queryStrings
					.get("productType"));
			if (!tags.contains(null) && !tags.contains("")) {
				context.getFilter().add(new ProductTagsFilter(tags));
			}
		}

		if (!queryStrings.containsKey("popularity")
				&& !queryStrings.containsKey("price")
				&& !queryStrings.containsKey("sale")
				&& !queryStrings.containsKey("review")
				&& !queryStrings.containsKey("discount")) {
			context.getSort().add(
					new ViewCountSortOrder(isAsc(queryStrings, "popularity")));
		}
		if (queryStrings.containsKey("storageid")) {
			List<String> storageids = Lists.newArrayList(queryStrings
					.get("storageid"));
			// Logger.debug("Category Filter: {}", category);
			if (!storageids.contains(null) && !storageids.contains("")) {
				context.getFilter().add(new StorageFilter(storageids));
			}
		}
	}

	private List<Date> getDates(String[] dates) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return FluentIterable.from(Lists.newArrayList(dates)).transform(s -> {
			try {
				return df.parse(s);
			} catch (Exception e) {
				return null;
			}
		}).toList();
	}

	private boolean isAsc(Map<String, String[]> queryStrings, String key) {
		if (queryStrings.containsKey(key)) {
			String[] values = queryStrings.get(key);
			if (values != null && values.length > 0) {
				return "asc".equalsIgnoreCase(values[0]);
			}
		}
		return false;
	}

	private int getInteger(Map<String, String[]> queryStrings, String key) {
		if (queryStrings.containsKey(key)) {
			String[] values = queryStrings.get(key);
			int num = 1;
			if (values != null && values.length > 0) {
				if (values[0].matches("[0-9]+")) {
					num = Integer.parseInt(values[0]);
				} else {
					Logger.error("-- search parameter must be int: {}--{}",
							key, values[0]);
				}
			}
			return num;
		}
		return 0;
	}

}
