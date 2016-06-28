package services.interaction;

import java.util.List;

import context.WebContext;
import dto.interaction.ProductBrowse;

public interface IMemberBrowseHistoryService {

	public abstract List<ProductBrowse> getMemberBrowseHistoryByContext(
			WebContext context, int items, boolean isLogin);
	
	public abstract boolean addMemberBrowseHistory(ProductBrowse p);

}