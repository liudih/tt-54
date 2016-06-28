package dao.manager.impl;

import java.util.List;

import javax.inject.Inject;

import mapper.category.CategoryProductRecommendMapper;
import dao.manager.ICategoryProductRecommendDao;
import dto.category.CategoryProductRecommend;

public class CategoryProductRecommendDao implements
		ICategoryProductRecommendDao {

	@Inject
	CategoryProductRecommendMapper mapper;

	@Override
	public int insertCategoryProductRecommend(CategoryProductRecommend cpr) {
		return mapper.insert(cpr);
	}

	@Override
	public List<CategoryProductRecommend> searchCategoryProductRecommend(
			Integer status, Integer parentid, Integer categoryid, String sku,
			String createdate, Integer siteid, String device) {
		return mapper.search(status, parentid, categoryid, sku, createdate,
				siteid, device);
	}

	public Integer searchMaxSequence(Integer categoryid, Integer siteid,
			String device) {
		return mapper.searchParentSequence(categoryid, siteid, device);
	}

	@Override
	public int updateStatus(Integer id, Integer status, String updateby) {
		return mapper.updateStatus(id, status, updateby);
	}

	@Override
	public int updateSequence(Integer categoryid, Integer sequence,
			Integer siteid, String device) {
		return mapper.updateSequence(categoryid, sequence, siteid, device);
	}

	@Override
	public int updateDeleteSequence(Integer categoryid, Integer sequence,
			Integer siteid, String sdevice) {
		return mapper.updateDeleteSequence(categoryid, sequence, siteid,
				sdevice);
	}

	@Override
	public int updateSequenceTop(Integer categoryid, Integer sequence,
			Integer siteid, String sdevice) {
		return mapper
				.updateSequenceOnTop(categoryid, sequence, siteid, sdevice);
	}

	@Override
	public int updateOnTop(Integer id, String updateby) {
		return mapper.updateOnTop(id, updateby);
	}

	@Override
	public String searchCategoryProductBySku(String sku, Integer categoryid) {
		return mapper.searchProductCategoryBySku(sku, categoryid);
	}

	@Override
	public int searchCategoryRecommendBySku(String sku, Integer categoryid,
			Integer siteid, String device) {
		return mapper.searchCategoryRecommendBySku(sku, categoryid, siteid,
				device);
	}

	@Override
	public CategoryProductRecommend searchCategoryRecommendById(Integer id) {
		return mapper.searchCategoryRecommendById(id);
	}

	@Override
	public List<CategoryProductRecommend> getCategoryRecommends() {
		// TODO Auto-generated method stub
		return mapper.getCategoryRecommends();
	}

	@Override
	public CategoryProductRecommend getcprByIid(Integer id) {
		// TODO Auto-generated method stub
		return mapper.getcprByIid(id);
	}

	@Override
	public List<CategoryProductRecommend> getCategoryRecommendsByCid(
			Integer categoryId) {
		// TODO Auto-generated method stub
		return mapper.getCategoryRecommendsByCid(categoryId);
	}
}
