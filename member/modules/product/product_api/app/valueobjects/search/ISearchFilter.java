package valueobjects.search;

import java.io.Serializable;

import org.elasticsearch.index.query.FilterBuilder;

public interface ISearchFilter extends Serializable {

	FilterBuilder getFilter();

}
