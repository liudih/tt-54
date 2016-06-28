package extensions.interaction.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import valueobjects.search.ISortOrder;

public class ReviewCountSortOrder implements ISortOrder {
	private static final long serialVersionUID = -2193649210387602021L;
	
	@Override
	public int sortOrder() {
		return 4;
	}
	
	boolean asc;

	public ReviewCountSortOrder() {

	}

	public ReviewCountSortOrder(boolean asc) {
		this.asc = asc;
	}

	@Override
	public SortBuilder getSortBuilder() {
		return SortBuilders.fieldSort("extras.interactions.reviewCount")
				.sortMode("min").order(asc ? SortOrder.ASC : SortOrder.DESC);
	}
	
	public boolean isAsc() {
		return asc;
	}

}
