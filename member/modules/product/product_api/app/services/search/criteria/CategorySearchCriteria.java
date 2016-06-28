package services.search.criteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchCriteria;

public class CategorySearchCriteria implements ISearchCriteria {

	/**
	 * 
	 */
	private static final long serialVersionUID = -858686739524117798L;

	int categoryId;

	public CategorySearchCriteria() {
	}

	public CategorySearchCriteria(Integer icategoryid) {
		this.categoryId = icategoryid;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return QueryBuilders.termQuery("categories", categoryId);
	}

}
