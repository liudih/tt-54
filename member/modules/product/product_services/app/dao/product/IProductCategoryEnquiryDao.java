package dao.product;

import java.util.Date;
import java.util.List;

import valueobjects.product.category.CategoryThreeLevel;
import dao.IProductEnquiryDao;
import dto.product.CategoryName;

public interface IProductCategoryEnquiryDao extends IProductEnquiryDao {

	public Integer getProductRootCategoryIdByListingId(String listingId,
			Integer languageId);

	public List<String> getAllListingIdsByRootId(Integer rootCategoryId);

	public CategoryName getCategoryNameByCategoryIdAndLanguage(
			Integer rootCategoryId, Integer languageId);

	public List<String> getAllListingIdsByRootIds(List<Integer> rootCategoryIds);

	public List<String> getListingIdsByRootId(Integer rootCategoryId,
			Integer pageSize, Integer pageNum);

	public List<String> getListingIds(List<Integer> rootCategoryId,
			Integer pageSize, Integer pageNum);

	/**
	 * 根具sku查询产品的三级类目名称
	 * 
	 * @param csku
	 * @param dcreatestartdate
	 *            产品上架时间
	 * @param dcreateenddate
	 *            产品结束上架时间
	 * @param ilangid
	 *            类目名称语言d
	 * @return
	 */
	List<CategoryThreeLevel> getProductThreeLevelCategoryBySku(
			List<String> cskus, Date dcreatestartdate, Date dcreateenddate,
			Integer ilangid);
}
