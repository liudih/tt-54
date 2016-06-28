package services.interaction;

import java.util.List;

import valueobjects.base.Page;
import valueobjects.interaction.CollectCount;
import dto.interaction.ProductCollect;

public interface ICollectService {

	public abstract List<ProductCollect> getCollectByMember(String lid,
			String email);

	public abstract boolean addCollect(String lid, String email);

	public abstract boolean delCollect(String lid, String email);

	public abstract boolean delCollectByListingids(String lids, String email);

	public abstract List<String> getCollectListingIDByEmail(String email);

	public abstract int getCountByListingID(String listingID);

	public abstract Page<ProductCollect> getCollectsPageByEmail(String email,
			Integer page, Integer pageSize, Integer language, String sort,
			String searchname, Integer categoryId);

	public abstract List<CollectCount> getCollectCountByListingIds(
			List<String> listingIds);

	public abstract List<ProductCollect> getCollectByListingIds(
			List<String> listingIds);

}