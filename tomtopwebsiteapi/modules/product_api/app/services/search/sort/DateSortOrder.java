package services.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import valueobjects.search.ISortOrder;

public class DateSortOrder implements ISortOrder {

	private static final long serialVersionUID = -2793981599591590640L;
	
	public int sortOrder(){
		return 10;
	}

	boolean asc;

	public DateSortOrder() {

	}

	public DateSortOrder(boolean asc) {
		this.asc = asc;
	}

	@Override
	public SortBuilder getSortBuilder() {
		return SortBuilders.fieldSort("createdate").order(
				asc ? SortOrder.ASC : SortOrder.DESC);
	}

	public boolean isAsc() {
		return asc;
	}

}
