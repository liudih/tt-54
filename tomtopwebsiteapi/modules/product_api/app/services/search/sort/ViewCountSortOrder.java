package services.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import valueobjects.search.ISortOrder;

public class ViewCountSortOrder implements ISortOrder {

	private static final long serialVersionUID = -4434678042978409902L;

	public int sortOrder(){
		return 7;
	}
	
	boolean asc;

	public ViewCountSortOrder() {
	}

	public ViewCountSortOrder(boolean asc) {
		this.asc = asc;
	}

	@Override
	public SortBuilder getSortBuilder() {
		return SortBuilders.fieldSort("viewCount").order(
				asc ? SortOrder.ASC : SortOrder.DESC);
	}

	public boolean isAsc() {
		return asc;
	}

}
