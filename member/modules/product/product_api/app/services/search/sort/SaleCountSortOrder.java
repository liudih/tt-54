package services.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import play.Logger;
import valueobjects.search.ISortOrder;

public class SaleCountSortOrder implements ISortOrder {

	private static final long serialVersionUID = 1L;

	public int sortOrder(){
		return 5;
	}
	
	boolean asc;

	public SaleCountSortOrder() {

	}

	public SaleCountSortOrder(boolean asc) {
		this.asc = asc;
	}

	@Override
	public SortBuilder getSortBuilder() {
		 SortBuilder s = SortBuilders.fieldSort("extras.sales.sale").sortMode("min")
				.order(asc ? SortOrder.ASC : SortOrder.DESC);
		 //Logger.debug("result:{}",s);
		 return s;
	}

	public boolean isAsc() {
		return asc;
	}

}
