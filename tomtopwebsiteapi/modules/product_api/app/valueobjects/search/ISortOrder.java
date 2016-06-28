package valueobjects.search;

import java.io.Serializable;

import org.elasticsearch.search.sort.SortBuilder;

public interface ISortOrder extends Serializable {

	SortBuilder getSortBuilder();
	
	int sortOrder();

}
