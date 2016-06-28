package services.search.filter;

import java.util.List;

import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;

import valueobjects.search.ISearchFilter;

import com.google.common.collect.Lists;

public class StorageFilter implements ISearchFilter {

	private static final long serialVersionUID = -2719583419571195493L;

	List<String> storageids;

	public StorageFilter() {
		this.storageids = Lists.newArrayList();
	}

	public StorageFilter(List<String> categoriesValuePair) {
		this.storageids = Lists.newArrayList(categoriesValuePair);
	}

	@Override
	public FilterBuilder getFilter() {
		BoolFilterBuilder bool = FilterBuilders.boolFilter();
		for (String id : storageids) {
			bool.should(FilterBuilders.termFilter("storagid", id));
		}
		return bool;
	}
	
	public List<String> getStorageIds(){
		return this.storageids;
	}

}
