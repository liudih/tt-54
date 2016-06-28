package services.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import valueobjects.search.ISortOrder;

public class DiscountSortOrder implements ISortOrder {

	private static final long serialVersionUID = -6285092391519644091L;
	
	public int sortOrder(){
		return 9;
	}
	
	boolean asc;

	public DiscountSortOrder() {
	}

	public DiscountSortOrder(boolean asc) {
		this.asc = asc;
	}

	@Override
	public SortBuilder getSortBuilder() {
		return SortBuilders
				.scriptSort(
						"def d = _source.price.discount.grep { "
								+ "it -> (new Date()).getTime() > it.fromDate && (new Date()).getTime() < it.toDate "
								+ "}*.discount;"
								+ "return (d.size() > 0 ? d.first() : 0)",
						"number").order(asc ? SortOrder.ASC : SortOrder.DESC);
	}

	public boolean isAsc() {
		return asc;
	}

}
