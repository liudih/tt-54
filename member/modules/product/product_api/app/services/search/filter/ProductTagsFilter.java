package services.search.filter;

import java.util.List;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

import services.search.criteria.ProductLabelType;
import valueobjects.search.ISearchFilter;

import com.google.common.collect.Lists;

public class ProductTagsFilter implements ISearchFilter {

	private static final long serialVersionUID = 1L;

	List<String> productTags;

	public ProductTagsFilter() {
		this.productTags = Lists.newArrayList();
	}

	public ProductTagsFilter(List<String> productTags) {
		this.productTags = Lists.newArrayList(productTags);
	}

	@Override
	public FilterBuilder getFilter() {
		BoolFilterBuilder bool = FilterBuilders.boolFilter();
		for (String tag : productTags) {
			bool.must(FilterBuilders.termFilter("tags", tag)).mustNot(
					FilterBuilders.termFilter("tags",
							ProductLabelType.Clearstocks.toString()));
		}
		return bool;
	}
}
