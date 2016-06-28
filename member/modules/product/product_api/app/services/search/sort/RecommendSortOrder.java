package services.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import valueobjects.search.ISortOrder;

public class RecommendSortOrder implements ISortOrder {

	private static final long serialVersionUID = -4434678042978409902L;

	public int sortOrder(){
		return 1;
	}
	
	boolean asc;
	
	Integer categoryId;
	
	int siteid;
	
	String device;

	public RecommendSortOrder() {
	}

	public RecommendSortOrder(boolean asc, Integer category, int siteid, String device) {
		this.asc = asc;
		this.categoryId = category;
		this.siteid = siteid;
		this.device = device;
	}
	
	@Override
	public SortBuilder getSortBuilder() {
		return SortBuilders
				.scriptSort(
						"def d = _source.categoryRecommend.grep { "
								+ "it -> it.categoryId=="+categoryId+" && it.siteid=="+siteid+" && it.device=='"+device+"'"
								+ "}*.sequence;"
								+ "return (d.size() > 0 ? d.first() : 9999)",
						"number").order(asc ? SortOrder.ASC : SortOrder.DESC);

	}

	public boolean isAsc() {
		return asc;
	}

}
