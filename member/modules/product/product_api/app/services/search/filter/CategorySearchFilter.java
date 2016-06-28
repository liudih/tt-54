package services.search.filter;

import java.util.List;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

import valueobjects.search.ISearchFilter;

import com.google.common.collect.Lists;

public class CategorySearchFilter implements ISearchFilter {

	private static final long serialVersionUID = -2719583419571195493L;

	List<String> categories;

	public CategorySearchFilter() {
		this.categories = Lists.newArrayList();
	}

	public CategorySearchFilter(List<String> categoriesValuePair) {
		this.categories = Lists.newArrayList(categoriesValuePair);
	}

	@Override
	public FilterBuilder getFilter() {
		BoolFilterBuilder bool = FilterBuilders.boolFilter();
		for (String category : categories) {
			bool.should(FilterBuilders.termFilter("categories", category));
		}
		return bool;
	}
	
	public List<String> getCategories(){
		return this.categories;
	}

}
