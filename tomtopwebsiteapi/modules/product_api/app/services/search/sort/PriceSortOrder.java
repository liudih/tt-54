package services.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import valueobjects.search.ISortOrder;

public class PriceSortOrder implements ISortOrder {

	private static final long serialVersionUID = -6285092391519644091L;

	public int sortOrder(){
		return 8;
	}
	
	boolean asc;

	public PriceSortOrder() {
	}

	public PriceSortOrder(boolean asc) {
		this.asc = asc;
	}

	@Override
	public SortBuilder getSortBuilder() {
		return SortBuilders
				.scriptSort(
						"def d = _source.price.discount.grep { "
								+ "it -> (new Date()).getTime() > it.fromDate && (new Date()).getTime() < it.toDate "
								+ "}*.price;"
								+ "return (d.size() > 0 ? d.first() : _source.price.basePrice)",
						"number").order(asc ? SortOrder.ASC : SortOrder.DESC);
	}

	public boolean isAsc() {
		return asc;
	}

}
