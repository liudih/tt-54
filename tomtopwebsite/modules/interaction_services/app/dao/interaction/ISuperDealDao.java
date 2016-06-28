package dao.interaction;

import java.util.List;

import dao.InteractionUpdateDao;
import dto.interaction.SuperDeal;

public interface ISuperDealDao extends InteractionUpdateDao {

	Integer addNewSuperDeal(SuperDeal superDeal);

	void deleteOldAutoGetSuperDealsByCreateUser(String string);

	List<dto.interaction.SuperDeal> getSuperDealByPageAndRootCategoryId(
			Integer page, Integer perPage, Integer rootCategoryId, String sku,
			Integer websiteId);

	Integer getSuperDealCount(Integer rootCategoryId, String sku,
			Integer websiteId);

	SuperDeal getSuperDealById(Integer id);

	boolean deleteSuperDealById(Integer id);

	List<String> getSuperDealListingIds(boolean isAuto, Integer limit,
			Integer websiteId);

	Boolean updateSuperDealBshow(Integer id, Boolean bshow);

	List<String> getAllSuperDealsListingIds(Integer websiteId);

	List<String> getListingIds(Integer websiteId, Integer pageNum,
			Integer pageSize);

	List<String> getSDPageBySiteId(int siteId, Integer rootCategoryId, Integer pageSize, int startIndex);

	int getSDCountBySiteId(int siteId, Integer rootCategoryId);
}
