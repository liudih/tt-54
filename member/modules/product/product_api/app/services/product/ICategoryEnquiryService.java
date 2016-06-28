package services.product;

import java.util.List;

import valueobjects.product.category.CategoryComposite;
import valueobjects.product.category.CategoryReverseComposite;
import context.WebContext;
import dto.product.CategoryBase;
import dto.product.CategoryName;
import dto.product.CategoryWebsiteWithName;
import dto.product.ProductCategoryMapper;

public interface ICategoryEnquiryService {

	public List<CategoryWebsiteWithName> getCategoryItemRootByDisplay(
			WebContext context, boolean display);

	public List<CategoryComposite> getRootCategories(final int language,
			final int siteId);

	public List<CategoryWebsiteWithName> getChildCategoriesByPath(String path,
			WebContext webContext);

	public List<CategoryWebsiteWithName> getChildCategories(Integer categoryId,
			WebContext context);

	CategoryReverseComposite getReverseCategory(int categoryId,
			WebContext context);

	List<ProductCategoryMapper> selectByListingId(String listingid);
	
	public CategoryName getCategoryNameByPath(String path, WebContext wc);

	public List<CategoryComposite> getNewChildCategory(int categoryId,
			int maxCategoryId, WebContext context, int depth);
	
	//通过品类编号，获取品类信息
	public CategoryBase getCategoryBaseByiid(int iid) ;
	
}
