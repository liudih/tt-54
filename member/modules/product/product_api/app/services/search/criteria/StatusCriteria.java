package services.search.criteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchCriteria;

public class StatusCriteria implements ISearchCriteria{
	
	private static final long serialVersionUID = -5944953527326692848L;
	int status;
	
	public StatusCriteria(){
		
	}
	
	public StatusCriteria(int status){
		this.status=status;
	}
	
	@Override
	public QueryBuilder getQueryBuilder() {
		return QueryBuilders.termQuery("status",status);
	}
}
