package services.search.sort;

import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import play.Logger;
import scala.collection.Map;
import valueobjects.search.ISortOrder;

public class TagDateSortOrder implements ISortOrder {

	private static final long serialVersionUID = -4800240355984179009L;

	public int sortOrder() {
		return 6;
	}

	String productTag;

	public TagDateSortOrder() {
	}

	public TagDateSortOrder(String productTag) {
		this.productTag = productTag;
	}

	@Override
	public SortBuilder getSortBuilder() {
		return SortBuilders.scriptSort(
				"return (_source.tagsType.get('" + this.productTag
						+ "')!=null ? _source.tagsType.get('" + this.productTag
						+ "').tagDate : '0')", "number").order(SortOrder.DESC);
	}

}
