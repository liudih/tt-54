package dao.interaction.impl;

import java.util.List;

import mapper.interaction.SuperDealMapper;

import com.google.inject.Inject;

import dao.interaction.ISuperDealDao;
import dto.interaction.SuperDeal;

public class SuperDealDao implements ISuperDealDao {

	@Inject
	SuperDealMapper superDealMapper;

	@Override
	public Integer addNewSuperDeal(SuperDeal superDeal) {
		return superDealMapper.insertSelective(superDeal);
	}

	@Override
	public void deleteOldAutoGetSuperDealsByCreateUser(String createUserName) {
		superDealMapper.deleteOldAutoGetSuperDealsByCreateUser(createUserName);
	}

	@Override
	public List<SuperDeal> getSuperDealByPageAndRootCategoryId(Integer page,
			Integer perPage, Integer rootCategoryId, String sku,
			Integer websiteId) {
		return superDealMapper.getSuperDealByPageAndRootCategoryIdAndListingId(
				page, perPage, rootCategoryId, sku, websiteId);
	}

	@Override
	public SuperDeal getSuperDealById(Integer id) {
		return superDealMapper.getSuperDealById(id);
	}

	@Override
	public boolean deleteSuperDealById(Integer id) {
		int result = superDealMapper.deleteSuperDealById(id);
		return result > 0 ? true : false;
	}

	@Override
	public Integer getSuperDealCount(Integer rootCategoryId, String sku,
			Integer websiteId) {
		return superDealMapper
				.getSuperDealCount(rootCategoryId, sku, websiteId);
	}

	@Override
	public List<String> getSuperDealListingIds(boolean isAuto, Integer limit,
			Integer websiteId) {
		return superDealMapper.getSuperDealListingIds(isAuto, limit, websiteId);
	}

	@Override
	public Boolean updateSuperDealBshow(Integer id, Boolean bshow) {
		return superDealMapper.updateSuperDealBshow(id, bshow);
	}

	@Override
	public List<String> getAllSuperDealsListingIds(Integer websiteId) {
		return superDealMapper.getAllSuperDealsListingIds(websiteId);
	}

	@Override
	public List<String> getListingIds(Integer websiteId, Integer pageNum,
			Integer pageSize) {
		return superDealMapper.getListingIds(websiteId, pageNum, pageSize);
	}

	@Override
	public List<String> getSDPageBySiteId(int siteId, Integer rootCategoryId, Integer pageSize, int startIndex) {
		return superDealMapper.getSDPageBySiteId(siteId,rootCategoryId,pageSize,startIndex);
	}

	@Override
	public int getSDCountBySiteId(int siteId, Integer rootCategoryId) {
		return superDealMapper.getSDCountBySiteId(siteId,rootCategoryId);
	}

}
