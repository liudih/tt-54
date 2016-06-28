package dao.manager;

import java.util.List;

import dto.category.CategoryProductRecommend;

public interface ICategoryProductRecommendDao {

	public int insertCategoryProductRecommend(CategoryProductRecommend cpr);

	public List<CategoryProductRecommend> searchCategoryProductRecommend(
			Integer status, Integer parentid, Integer categoryid, String sku,
			String createdate, Integer siteid, String device);

	public Integer searchMaxSequence(Integer categoryid, Integer siteid,
			String device);

	public int updateStatus(Integer id, Integer status, String updateby);

	public int updateSequence(Integer categoryid, Integer sequence,
			Integer siteid, String device);

	public int updateDeleteSequence(Integer categoryid, Integer sequence,
			Integer siteid, String sdevice);

	public int updateSequenceTop(Integer categoryid, Integer sequence,
			Integer siteid, String sdevice);

	public int updateOnTop(Integer id, String updateby);

	public String searchCategoryProductBySku(String sku, Integer categoryid);

	public int searchCategoryRecommendBySku(String sku, Integer categoryid,
			Integer siteid, String device);

	public CategoryProductRecommend searchCategoryRecommendById(Integer id);
	
	public List<CategoryProductRecommend> getCategoryRecommends();
	
	public CategoryProductRecommend getcprByIid(Integer id);
	
	public List<CategoryProductRecommend> getCategoryRecommendsByCid(Integer categoryId);
}
