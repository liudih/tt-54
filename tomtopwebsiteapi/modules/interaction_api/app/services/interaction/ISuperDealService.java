package services.interaction;

import java.util.List;

import context.WebContext;

public interface ISuperDealService {
	public abstract List<String> getListingIdsByPage(Integer pageNum,
			Integer pageSize, WebContext context);
	
	
	public abstract List<String> getSuperDealListingIds(Integer limit,
			Integer websiteId);
}