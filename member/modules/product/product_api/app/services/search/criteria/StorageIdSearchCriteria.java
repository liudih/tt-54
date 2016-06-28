package services.search.criteria;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import valueobjects.search.ISearchCriteria;

public class StorageIdSearchCriteria implements ISearchCriteria {

	private static final long serialVersionUID = 5984627393788218386L;

	int storageId;

	public StorageIdSearchCriteria() {

	}

	public StorageIdSearchCriteria(Integer storageId) {
		this.storageId = storageId;
	}

	@Override
	public QueryBuilder getQueryBuilder() {
		return QueryBuilders.termQuery("storagid", storageId);
	}
}
