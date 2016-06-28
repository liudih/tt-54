package valueobjects.search;

import java.io.Serializable;

import org.elasticsearch.index.query.QueryBuilder;

public interface ISearchCriteria extends Serializable {

	QueryBuilder getQueryBuilder();

}
